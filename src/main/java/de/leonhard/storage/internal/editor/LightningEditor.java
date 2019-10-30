package de.leonhard.storage.internal.editor;

import de.leonhard.storage.internal.enums.Comment;
import de.leonhard.storage.internal.enums.DataType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Class for parsing a Lightning-Type File
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightningEditor {

	/**
	 * Write the given Data to a File.
	 *
	 * @param file           the File to be written to.
	 * @param map            a HashMap containing the Data to be written.
	 * @param commentSetting the ConfigSetting to be used.
	 */
	public static void writeData(@NotNull final File file, @NotNull final Map<String, Object> map, @NotNull final Comment commentSetting) {
		if (commentSetting == Comment.PRESERVE) {
			initialWriteWithComments(file, map);
		} else if (commentSetting == Comment.SKIP) {
			initialWriteWithOutComments(file, map);
		} else {
			throw new IllegalArgumentException("Illegal ConfigSetting");
		}
	}

	/**
	 * Read the Data of a File.
	 *
	 * @param file           the File to be read from.
	 * @param dataType       the FileDataType to be used.
	 * @param commentSetting the ConfigSetting to be used.
	 * @return a Map containing the Data of the File.
	 */
	public static Map<String, Object> readData(@NotNull final File file, @NotNull final DataType dataType, @NotNull final Comment commentSetting) {
		if (commentSetting == Comment.PRESERVE) {
			return initialReadWithComments(file, dataType, commentSetting);
		} else if (commentSetting == Comment.SKIP) {
			return initialReadWithOutComments(file, dataType, commentSetting);
		} else {
			throw new IllegalArgumentException("Illegal ConfigSetting");
		}
	}

	// <Read Data>
	// <Read Data with Comments>
	private static Map<String, Object> initialReadWithComments(@NotNull final File file, @NotNull final DataType dataType, @NotNull final Comment commentSetting) {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

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
					tempMap.put(tempKey, internalReadWithComments(file.getAbsolutePath(), lines, blankLine, commentLine, dataType, commentSetting));
				} else {
					tempKey = readKey(file.getAbsolutePath(), lines, dataType, commentSetting, tempMap, tempKey, tempLine);
				}
			}
			return tempMap;
		} catch (IOException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error while reading '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	private static Map<String, Object> internalReadWithComments(final String filePath, final List<String> lines, int blankLine, int commentLine, final DataType dataType, final Comment commentSetting) throws ArrayIndexOutOfBoundsException {
		Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
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
				commentLine++;
				tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new IllegalStateException("Error at '" + filePath + "' -> Key must not be null");
				}
				tempMap.put(tempKey, internalReadWithComments(filePath, lines, blankLine, commentLine, dataType, commentSetting));
			} else {
				tempKey = readKey(filePath, lines, dataType, commentSetting, tempMap, tempKey, tempLine);
			}
		}
		throw new IllegalStateException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read Data with Comments>

	// <Read Data without Comments>
	private static Map<String, Object> initialReadWithOutComments(@NotNull final File file, @NotNull final DataType dataType, @NotNull final Comment commentSetting) {
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);

			String tempKey = null;
			while (lines.size() > 0) {
				String tempLine = lines.get(0).trim();
				lines.remove(0);

				if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
					if (tempLine.contains("}")) {
						throw new IllegalStateException("Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
					} else if (tempLine.endsWith("{")) {
						if (!tempLine.equals("{")) {
							tempKey = tempLine.replace("{", "").trim();
						} else if (tempKey == null) {
							throw new IllegalStateException("Error at '" + file.getAbsolutePath() + "' -> Key must not be null");
						}
						tempMap.put(tempKey, internalReadWithOutComments(file.getAbsolutePath(), lines, dataType, commentSetting));
					} else {
						tempKey = readKey(file.getAbsolutePath(), lines, dataType, commentSetting, tempMap, tempKey, tempLine);
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

	private static Map<String, Object> internalReadWithOutComments(final String filePath, final List<String> lines, final DataType dataType, final Comment commentSetting) throws ArrayIndexOutOfBoundsException {
		Map<String, Object> tempMap = dataType.getNewDataMap(commentSetting, null);
		String tempKey = null;

		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (!tempLine.isEmpty() && !tempLine.startsWith("#")) {
				if (tempLine.equals("}")) {
					return tempMap;
				} else if (tempLine.contains("}")) {
					throw new IllegalStateException("Error at '" + filePath + "' -> Block closed without being opened");
				} else if (tempLine.endsWith("{")) {
					if (!tempLine.equals("{")) {
						tempKey = tempLine.replace("{", "").trim();
					} else if (tempKey == null) {
						throw new IllegalStateException("Error at '" + filePath + "' -> Key must not be null");
					}
					tempMap.put(tempKey, internalReadWithOutComments(filePath, lines, dataType, commentSetting));
				} else {
					tempKey = readKey(filePath, lines, dataType, commentSetting, tempMap, tempKey, tempLine);
				}
			}
		}
		throw new IllegalStateException("Error at '" + filePath + "' -> Block does not close");
	}
	// </Read without Comments>

	private static String readKey(String filePath, List<String> lines, DataType dataType, Comment commentSetting, Map<String, Object> tempMap, String tempKey, String tempLine) {
		if (tempLine.contains("=")) {
			String[] line = tempLine.split("=");
			line[0] = line[0].trim();
			line[1] = line[1].trim();
			if (line[1].startsWith("[")) {
				if (line[1].endsWith("]")) {
					String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
					List<String> list = dataType.getNewDataList(commentSetting, null);
					for (String value : listArray) {
						list.add(value.trim());
					}
					tempMap.put(line[0], list);
				} else {
					tempMap.put(line[0], readList(filePath, lines, dataType, commentSetting));
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
		return tempKey;
	}


	private static List<String> readList(final String filePath, final List<String> lines, final DataType dataType, final Comment commentSetting) {
		List<String> localList = dataType.getNewDataList(commentSetting, null);
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
	private static void initialWriteWithComments(final File file, final Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				topLayerWriteWithComments(writer, map, (String) mapIterator.next());
				//noinspection unchecked
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

	private static void topLayerWriteWithComments(final PrintWriter writer, final Map<String, Object> map, final String localKey) {
		if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
			writer.print(localKey.substring(0, localKey.lastIndexOf("{=}")));
		} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
			writer.print("");
		} else if (map.get(localKey) instanceof Map) {
			writer.print(localKey + " " + "{");
			//noinspection unchecked
			internalWriteWithComments((Map<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private static void internalWriteWithComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
				writer.print(indentationString + "  " + localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
				writer.print("");
			} else {
				if (map.get(localKey) instanceof Map) {
					writer.print(indentationString + "  " + localKey + " " + "{");
					//noinspection unchecked
					internalWriteWithComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					//noinspection unchecked
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
	private static void initialWriteWithOutComments(final File file, final Map<String, Object> map) {
		try (PrintWriter writer = new PrintWriter(file)) {
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
			System.err.println("Could not write to '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
		}
	}

	private static void topLayerWriteWithOutComments(final PrintWriter writer, final Map<String, Object> map, final String localKey) {
		if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
			if (map.get(localKey) instanceof Map) {
				writer.print(localKey + " " + "{");
				//noinspection unchecked
				internalWriteWithoutComments((Map<String, Object>) map.get(localKey), "", writer);
			} else if (map.get(localKey) instanceof List) {
				writer.println("  " + localKey + " = [");
				//noinspection unchecked
				writeList((List<String>) map.get(localKey), "  ", writer);
			} else {
				writer.print(localKey + " = " + map.get(localKey));
			}
		}
	}

	private static void internalWriteWithoutComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
				if (map.get(localKey) instanceof Map) {
					writer.print(indentationString + "  " + localKey + " " + "{");
					//noinspection unchecked
					internalWriteWithoutComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					//noinspection unchecked
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

	private static void writeList(final List<String> list, final String indentationString, final PrintWriter writer) {
		for (String line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}
	// </Write Data>


	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}