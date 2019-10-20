package de.leonhard.storage.internal.editor;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.exceptions.InvalidSettingException;
import de.leonhard.storage.internal.base.exceptions.LightningFileReadException;
import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.LineType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class LightningFileEditor {

	private final File file;

	public LightningFileEditor(final File file) {
		this.file = file;
	}

	public Map<String, Object> readData(final ConfigSetting configSetting) throws LightningFileReadException, IOException, InvalidSettingException {
		switch (configSetting) {
			case PRESERVE_COMMENTS:
				return initialReadWithComments();
			case SKIP_COMMENTS:
				return initialReadWithOutComments();
			default:
				throw new InvalidSettingException("No proper ConfigSetting");
		}
	}

	public void writeData(final FileData fileData, final ConfigSetting configSetting) throws InvalidSettingException {
		switch (configSetting) {
			case PRESERVE_COMMENTS:
				initalWriteWithComments(fileData);
			case SKIP_COMMENTS:
				initalWriteWithOutComments(fileData);
			default:
				throw new InvalidSettingException("No proper ConfigSetting");
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private Map<String, Object> initialReadWithComments() throws LightningFileReadException, IOException {
		List<String> lines = Files.readAllLines(this.file.toPath());
		Map<String, Object> tempMap = new HashMap<>();
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
				tempMap.put(tempKey, readWithComments(lines, blankLine));
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
	private Map<String, Object> readWithComments(List<String> lines, int blankLine) throws LightningFileReadException {
		Map<String, Object> tempMap = new HashMap<>();
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
				tempMap.put(tempKey, readWithComments(lines, blankLine));
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

	@SuppressWarnings("DuplicatedCode")
	private Map<String, Object> initialReadWithOutComments() throws LightningFileReadException, IOException {
		List<String> lines = Files.readAllLines(this.file.toPath());
		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;
		while (lines.size() > 0) {
			String tempLine = lines.get(0).replaceAll("	", " ").trim();
			lines.remove(0);

			if (tempLine.contains("}")) {
				throw new LightningFileReadException("Block closed without being opened");
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new LightningFileReadException("Key must not be null");
				}
				tempMap.put(tempKey, readWithOutComments(lines));
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
	private Map<String, Object> readWithOutComments(List<String> lines) throws LightningFileReadException {
		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;

		while (lines.size() > 0) {
			String tempLine = lines.get(0).replaceAll("	", " ").trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new LightningFileReadException("Block closed without being opened");
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new LightningFileReadException("Key must not be null");
				}
				tempMap.put(tempKey, readWithOutComments(lines));
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

	private void initalWriteWithComments(final FileData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			Map<String, Object> map = fileData.toMap();
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

	private void topLayerWriteWithComments(PrintWriter writer, Map<String, Object> map, String localKey) {
		if (localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT)) {
			writer.print(localKey);
		} else if (localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
			writer.print("");
		} else if (map.get(localKey) instanceof Map) {
			writer.print(localKey + " " + "{");
			//noinspection unchecked
			writeWithComments((Map<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private void writeWithComments(Map<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (localKey.startsWith("#") && map.get(localKey).equals(LineType.COMMENT)) {
				writer.print(indentationString + "  " + localKey);
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey).equals(LineType.BLANK_LINE)) {
				writer.print("");
			} else if (map.get(localKey) instanceof Map) {
				writer.print(indentationString + "  " + localKey + " " + "{");
				//noinspection unchecked
				writeWithComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
			} else {
				writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}

	private void initalWriteWithOutComments(final FileData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			Map<String, Object> map = fileData.toMap();
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

	private void topLayerWriteWithOutComments(PrintWriter writer, Map<String, Object> map, String localKey) {
		if (map.get(localKey) instanceof Map) {
			writer.print(localKey + " " + "{");
			//noinspection unchecked
			writeWithOutComments((Map<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private void writeWithOutComments(Map<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (map.get(localKey) instanceof Map) {
				writer.print(indentationString + "  " + localKey + " " + "{");
				//noinspection unchecked
				writeWithOutComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
			} else {
				writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
}