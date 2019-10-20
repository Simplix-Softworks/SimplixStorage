package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"unused"})
public class LightningFile extends FlatFile {


	public LightningFile(final String name, final String path) {
		this(name, path, null, null);
	}

	public LightningFile(final String name, final String path, final InputStream inputStream, final ReloadSettings reloadSettings) {
		if (create(name, path, FileType.LS)) {
			if (inputStream != null) {
				FileUtils.writeToFile(this.file, inputStream);
			}
		}

		update();
		if (reloadSettings != null) {
			setReloadSettings(reloadSettings);
		}
	}


	@Override
	public Object get(final String key) {
		reload();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public synchronized void set(final String key, final Object value) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		reload();

		String oldData = fileData.toString();
		fileData.insert(finalKey, value);

		if (!oldData.equals(fileData.toString())) {
			write();
		}
	}

	@Override
	public boolean contains(String key) {
		return false;
	}

	@Override
	public synchronized void remove(final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		reload();

		fileData.remove(finalKey);

		write();
	}

	@Override
	protected void update() {

	}

	@Override
	public void reload() {
		if (shouldReload()) {
			try {
				this.fileData = new FileData(this.read());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	protected final LightningFile getLightningInstance() {
		return this;
	}


	private Map<String, Object> read() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		String line = reader.readLine();

		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;
		int blankLine = -1;
		while (line != null) {
			String tempLine = line.trim();
			line = reader.readLine();

			if (tempLine.contains("}")) {
				throw new IllegalStateException("Block closed without being opened");
			} else if (tempLine.isEmpty() || !tempLine.matches("\\S")) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, null);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine, null);
			} else if (tempLine.endsWith("{")) {
				if (tempLine.equals("{") && tempKey == null) {
					throw new IllegalStateException("Key must not be null");
				} else {
					tempKey = tempLine.replace("{", "").trim();
				}
				tempMap.put(tempKey, read(line, blankLine, reader));
			} else {
				if (tempLine.contains(" = ")) {
					String[] localLine = tempLine.split(" = ");
					tempMap.put(localLine[0], localLine[1]);
				} else {
					if (line.trim().equals("{")) {
						tempKey = tempLine;
					} else {
						throw new IllegalStateException("Key does not contain value or block");
					}
				}
			}
		}
		reader.close();
		return tempMap;
	}

	private Map<String, Object> read(String line, int blankLine, final BufferedReader reader) throws IllegalStateException, IOException {
		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;

		while (line != null) {
			String tempLine = line.trim();
			line = reader.readLine();

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new IllegalStateException("Block closed without being opened");
			} else if (tempLine.isEmpty() || !tempLine.matches("\\S")) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, null);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine, null);
			} else if (tempLine.endsWith("{")) {
				if (tempLine.equals("{") && tempKey == null) {
					throw new IllegalStateException("Key must not be null");
				} else {
					tempKey = tempLine.replace("{", "").trim();
				}
				tempMap.put(tempKey, read(line, blankLine, reader));
			} else {
				if (tempLine.contains(" = ")) {
					String[] localLine = tempLine.split(" = ");
					tempMap.put(localLine[0], localLine[1]);
				} else {
					if (line.trim().equals("{")) {
						tempKey = tempLine;
					} else {
						throw new IllegalStateException("Key does not contain value or block");
					}
				}
			}
		}
		reader.close();
		throw new IllegalStateException("Block does not close");
	}


	private void write() {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			Map<String, Object> map = this.fileData.toMap();
			for (String localKey : map.keySet()) {
				if (localKey.startsWith("#") && map.get(localKey) == null) {
					writer.println(localKey);
				} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
					writer.println("");
				} else if (map.get(localKey) instanceof Map) {
					writer.println(localKey + " " + "{");
					//noinspection unchecked
					write((Map<String, Object>) map.get(localKey), "", writer);
				} else {
					writer.println(localKey + " = " + map.get(localKey));
				}
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Error while writing to '" + this.file.getName() + "'");
			e.printStackTrace();
		}
	}

	private void write(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (String localKey : map.keySet()) {
			if (localKey.startsWith("#") && map.get(localKey) == null) {
				writer.println(indentationString + "  " + localKey);
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
				writer.println("");
			} else if (map.get(localKey) instanceof Map) {
				writer.println(indentationString + "  " + localKey + " " + "{");
				//noinspection unchecked
				write((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
			} else {
				writer.println(indentationString + "  " + localKey + " = " + map.get(localKey));
			}
		}
		writer.println(indentationString + "}");
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningFile lightningFile = (LightningFile) obj;
			return super.equals(lightningFile.getFlatFileInstance());
		}
	}
}