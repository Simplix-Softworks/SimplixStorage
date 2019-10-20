package de.leonhard.storage.internal.datafiles.editor;

import de.leonhard.storage.internal.base.data.SortedData;
import de.leonhard.storage.internal.base.enums.ConfigSetting;
import de.leonhard.storage.internal.base.enums.LineType;
import de.leonhard.storage.internal.base.exceptions.InvalidSettingException;
import de.leonhard.storage.internal.base.exceptions.LightningFileReadException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


@SuppressWarnings("unused")
public class LightningFileEditor {

	private final File file;

	public LightningFileEditor(final File file) {
		this.file = file;
	}

	public LinkedHashMap<String, Object> readData(final ConfigSetting configSetting) throws LightningFileReadException, IOException, InvalidSettingException {
		return initialRead();
	}

	public void writeData(final SortedData fileData, final ConfigSetting configSetting) throws InvalidSettingException {
		if (configSetting.equals(ConfigSetting.PRESERVE_COMMENTS)) {
			initalWriteWithComments(fileData);
		} else if (configSetting.equals(ConfigSetting.SKIP_COMMENTS)) {
			initalWriteWithOutComments(fileData);
		} else {
			throw new InvalidSettingException("No proper ConfigSetting");
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private LinkedHashMap<String, Object> initialRead() throws LightningFileReadException, IOException {
		List<String> lines = Files.readAllLines(this.file.toPath());
		LinkedHashMap<String, Object> tempMap = new LinkedHashMap<>();
		String tempKey = null;
		int blankLine = -1;
		while (lines.size() > 0) {
			String tempLine = lines.get(0).replaceAll("	", " ").trim();
			lines.remove(0);

			if (tempLine.contains("}")) {
				throw new LightningFileReadException("Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new LightningFileReadException("Key must not be null");
				}
				tempMap.put(tempKey, read(lines, blankLine));
			} else {
				if (tempLine.contains(" = ")) {
					String[] line = tempLine.split(" = ");
					tempMap.put(line[0], line[1]);
				} else {
					if (lines.get(1).contains("{")) {
						tempKey = tempLine;
					} else {
						throw new LightningFileReadException("Key does not contain value or block");
					}
				}
			}
		}
		return tempMap;
	}

	@SuppressWarnings("DuplicatedCode")
	private LinkedHashMap<String, Object> read(List<String> lines, int blankLine) throws LightningFileReadException {
		LinkedHashMap<String, Object> tempMap = new LinkedHashMap<>();
		String tempKey = null;

		while (lines.size() > 0) {
			String tempLine = lines.get(0).replaceAll("	", " ").trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new LightningFileReadException("Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new LightningFileReadException("Key must not be null");
				}
				tempMap.put(tempKey, read(lines, blankLine));
			} else {
				if (tempLine.contains(" = ")) {
					String[] line = tempLine.split(" = ");
					tempMap.put(line[0], line[1]);
				} else {
					if (lines.get(1).contains("{")) {
						tempKey = tempLine;
					} else {
						throw new LightningFileReadException("Key does not contain value or block");
					}
				}
			}
		}
		throw new LightningFileReadException("Block does not close");
	}

	private void initalWriteWithComments(final SortedData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			LinkedHashMap<String, Object> map = fileData.toMap();
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithComments(writer, map, mapIterator.next().toString());
				//noinspection unchecked
				mapIterator.forEachRemaining(localKey -> {
					writer.println();
					topLayerWriteWithComments(writer, map, localKey.toString());
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Error while writing to '" + file.getName() + "'");
			e.printStackTrace();
		}
	}

	private void topLayerWriteWithComments(PrintWriter writer, LinkedHashMap<String, Object> map, String localKey) {
		if (localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT)) {
			writer.print(localKey);
		} else if (localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
			writer.print("");
		} else if (map.get(localKey) instanceof LinkedHashMap) {
			writer.print(localKey + " " + "{");
			//noinspection unchecked
			writeWithComments((LinkedHashMap<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private void writeWithComments(LinkedHashMap<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT)) {
				writer.print(indentationString + "  " + localKey);
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
				writer.print("");
			} else {
				localWrite(map, indentationString, writer, localKey);
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}

	private void initalWriteWithOutComments(final SortedData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			LinkedHashMap<String, Object> map = fileData.toMap();
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithOutComments(writer, map, mapIterator.next().toString());
				//noinspection unchecked
				mapIterator.forEachRemaining(localKey -> {
					writer.println();
					topLayerWriteWithOutComments(writer, map, localKey.toString());
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Error while writing to '" + file.getName() + "'");
			e.printStackTrace();
		}
	}

	private void topLayerWriteWithOutComments(PrintWriter writer, LinkedHashMap<String, Object> map, String localKey) {
		if (!localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT) && !localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
			if (map.get(localKey) instanceof LinkedHashMap) {
				writer.print(localKey + " " + "{");
				//noinspection unchecked
				writeWithComments((LinkedHashMap<String, Object>) map.get(localKey), "", writer);
			} else {
				writer.print(localKey + " = " + map.get(localKey));
			}
		}
	}

	private void writeWithOutComments(LinkedHashMap<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (!localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT) && !localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
				localWrite(map, indentationString, writer, localKey);
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}

	private void localWrite(LinkedHashMap<String, Object> map, String indentationString, PrintWriter writer, String localKey) {
		if (map.get(localKey) instanceof LinkedHashMap) {
			writer.print(indentationString + "  " + localKey + " " + "{");
			//noinspection unchecked
			writeWithComments((LinkedHashMap<String, Object>) map.get(localKey), indentationString + "  ", writer);
		} else {
			writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
		}
	}
}