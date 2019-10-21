package de.leonhard.storage.internal.editor;

import de.leonhard.storage.internal.FileData;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightningEditor {

	private final File file;

	public LightningEditor(final File file) {
		this.file = file;
	}

	public List<String> readLines() throws IOException {
		final byte[] bytes = Files.readAllBytes(file.toPath());
		return Arrays.asList(new String(bytes).split("\n"));
	}

	public Map<String, Object> readData() throws IOException {
		final byte[] bytes = Files.readAllBytes(file.toPath());
		final Object obj = new String(bytes).replace("\n", "");
		return (Map<String, Object>) obj;
	}


	public Map<String, Object> read() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		String line = reader.readLine();

		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;
		int blankLine = -1;
		while (line != null) {
			String tempLine = line.trim();
			line = reader.readLine();

			if (tempLine.contains("}")) {
				System.out.println(file.getName());
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


	public void write(final FileData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			Map<String, Object> map = fileData.toMap();
			for (String localKey : map.keySet()) {
				if (localKey.startsWith("#") && map.get(localKey) == null) {
					writer.println(localKey);
				} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
					writer.println("");
				} else if (map.get(localKey) instanceof Map) {
					writer.println(localKey + " " + "{");
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

	public void write(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (String localKey : map.keySet()) {
			if (localKey.startsWith("#") && map.get(localKey) == null) {
				writer.println(indentationString + "  " + localKey);
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
				writer.println("");
			} else if (map.get(localKey) instanceof Map) {
				writer.println(indentationString + "  " + localKey + " " + "{");
				write((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
			} else {
				writer.println(indentationString + "  " + localKey + " = " + map.get(localKey));
			}
		}
		writer.println(indentationString + "}");
	}
}