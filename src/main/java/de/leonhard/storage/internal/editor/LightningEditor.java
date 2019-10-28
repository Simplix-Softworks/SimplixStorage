package de.leonhard.storage.internal.editor;

import com.sun.istack.internal.NotNull;
import de.leonhard.storage.internal.settings.ConfigSettings;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;


/**
 * Class for parsing a Lightning-Type File
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class LightningEditor {

	private final File file;

	/**
	 * Write the given Data to a File.
	 *
	 * @param map           a HashMap containing the Data to be written.
	 * @param configSetting the ConfigSetting to be used.
	 */
	public void writeData(@NotNull Map<String, Object> map, @NotNull ConfigSettings configSetting) {
		if (configSetting == ConfigSettings.PRESERVE_COMMENTS) {
			initialWriteWithComments(file, map);
		} else if (configSetting == ConfigSettings.SKIP_COMMENTS) {
			initialWriteWithOutComments(file, map);
		} else {
			throw new IllegalArgumentException("Illegal ConfigSetting");
		}
	}

	/**
	 * Read the Data of a File.
	 *
	 * @return a Map containing the Data of the File.
	 */
	public Map<String, Object> readData() {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			Map<String, Object> tempMap = new HashMap<>();

			String tempKey = null;
			int blankLine = -1;
			int commentLine = -1;
			while (lines.size() > 0) {
				String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (tempLine.contains("}")) {
					throw new IllegalStateException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
				} else if (tempLine.isEmpty()) {
					blankLine++;
					tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
				} else if (tempLine.startsWith("#")) {
					commentLine++;
					tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new IllegalStateException("Error at '" + file.getAbsolutePath() + "' -> Key must not be null");
					}
					tempMap.put(tempKey, internalRead(file.getAbsolutePath(), lines, blankLine, commentLine));
				} else {
					if (tempLine.contains("=")) {
						String[] line = tempLine.split("=");
						line[0] = line[0].trim();
						line[1] = line[1].trim();
						if (line[1].startsWith("[")) {
							if (line[1].endsWith("]")) {
								String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
								List<String> list = new ArrayList<>();
								for (String value : listArray) {
									list.add(value.trim());
								}
								tempMap.put(line[0], list);
							} else {
								tempMap.put(line[0], readList(file.getAbsolutePath(), lines));

							}
						} else {
							tempMap.put(line[0], line[1]);
						}
					} else {
						if (lines.get(1).contains("{")) {
							tempKey = tempLine;
						} else {
							throw new IllegalStateException("Error at '" + file.getAbsolutePath() + "' -> '" + tempLine + "' does not contain value or subblock");
						}
					}
				}
			}
			return tempMap;
		} catch (IOException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error while reading '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	// <Read Data>
	private Map<String, Object> internalRead(String filePath, List<String> lines, int blankLine, int commentLine) throws ArrayIndexOutOfBoundsException {
		Map<String, Object> tempMap = new HashMap<>();
		String tempKey = null;

		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new IllegalStateException("Error at '" + filePath + "' -> Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new IllegalStateException("Error at '" + filePath + "' -> Key must not be null");
				}
				tempMap.put(tempKey, internalRead(filePath, lines, blankLine, commentLine));
			} else {
				if (tempLine.contains("=")) {
					String[] line = tempLine.split("=");
					line[0] = line[0].trim();
					line[1] = line[1].trim();
					if (line[1].startsWith("[")) {
						if (line[1].endsWith("]")) {
							String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
							List<String> list = new ArrayList<>();
							for (String value : listArray) {
								list.add(value.trim());
							}
							tempMap.put(line[0], list);
						} else {
							tempMap.put(line[0], readList(filePath, lines));
						}
					} else {
						tempMap.put(line[0], line[1]);
					}
				} else {
					if (lines.get(1).contains("{")) {
						tempKey = tempLine;
					} else {
						throw new IllegalStateException("Error at '" + filePath + "' -> '" + tempLine + "' does not contain value or subblock");
					}
				}
			}
		}
		throw new IllegalStateException("Error at '" + filePath + "' -> Block does not close");
	}

	private List<String> readList(String filePath, List<String> lines) {
		List<String> localList = new ArrayList<>();
		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);
			if (tempLine.startsWith("-")) {
				localList.add(tempLine.substring(1).trim());
			} else if (tempLine.endsWith("]")) {
				return localList;
			} else {
				throw new IllegalStateException("Error at '" + filePath + "' -> List not closed properly");
			}
		}
		throw new IllegalStateException("Error at '" + filePath + "' -> List not closed properly");
	}
	// </Read Data>


	// <Write Data>
	// <Write Data with Comments>
	private void initialWriteWithComments(File file, Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithComments(writer, map, (String) mapIterator.next());
				mapIterator.forEachRemaining(localKey -> {
					writer.println();
					topLayerWriteWithComments(writer, map, (String) localKey);
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	private void topLayerWriteWithComments(PrintWriter writer, Map<String, Object> map, String localKey) {
		if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
			writer.print(localKey.substring(0, localKey.lastIndexOf("{=}")));
		} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
			writer.print("");
		} else if (map.get(localKey) instanceof Map) {
			writer.print(localKey + " " + "{");
			internalWriteWithComments((Map<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private void internalWriteWithComments(Map<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
				writer.print(indentationString + "  " + localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
				writer.print("");
			} else {
				if (map.get(localKey) instanceof Map) {
					writer.print(indentationString + "  " + localKey + " " + "{");
					internalWriteWithComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					writeList((List<String>) map.get(localKey), indentationString + "  ", writer);
				} else {
					writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data with Comments

	// <Write Data without Comments>
	private void initialWriteWithOutComments(File file, Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithOutComments(writer, map, mapIterator.next().toString());
				mapIterator.forEachRemaining(localKey -> {
					writer.println();
					topLayerWriteWithOutComments(writer, map, localKey.toString());
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
		}
	}

	private void topLayerWriteWithOutComments(PrintWriter writer, Map<String, Object> map, String localKey) {
		if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
			if (map.get(localKey) instanceof Map) {
				writer.print(localKey + " " + "{");
				internalWriteWithoutComments((Map<String, Object>) map.get(localKey), "", writer);
			} else if (map.get(localKey) instanceof List) {
				writer.println("  " + localKey + " = [");
				writeList((List<String>) map.get(localKey), "  ", writer);
			} else {
				writer.print(localKey + " = " + map.get(localKey));
			}
		}
	}

	private void internalWriteWithoutComments(Map<String, Object> map, String indentationString, PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
				if (map.get(localKey) instanceof Map) {
					writer.print(indentationString + "  " + localKey + " " + "{");
					internalWriteWithoutComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					writeList((List<String>) map.get(localKey), indentationString + "  ", writer);
				} else {
					writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data without Comments>

	private void writeList(List<String> list, String indentationString, PrintWriter writer) {
		for (String line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}
	// </Write Data>


	private enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}