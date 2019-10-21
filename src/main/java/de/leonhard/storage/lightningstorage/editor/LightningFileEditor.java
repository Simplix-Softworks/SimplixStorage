package de.leonhard.storage.lightningstorage.editor;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class LightningFileEditor {

	private final File file;
	@Getter
	@Setter
	private FlatFile.ConfigSetting configSetting;
	@Getter
	@Setter
	private FileData.Type fileDataType;

	public LightningFileEditor(@NotNull final File file, @NotNull final FlatFile.ConfigSetting configSetting, @NotNull final FileData.Type fileDataType) {
		this.file = file;
		this.configSetting = configSetting;
		this.fileDataType = fileDataType;
	}

	public void writeData(final FileData fileData) {
		if (configSetting == FlatFile.ConfigSetting.PRESERVE_COMMENTS) {
			this.initalWriteWithComments(fileData);
		} else if (configSetting == FlatFile.ConfigSetting.SKIP_COMMENTS) {
			this.initalWriteWithOutComments(fileData);
		} else {
			throw new IllegalArgumentException("Illegal ConfigSetting");
		}
	}

	@SuppressWarnings("DuplicatedCode")
	public Map<String, Object> readData() throws IOException {
		List<String> lines = Files.readAllLines(this.file.toPath());
		Map<String, Object> tempMap = this.fileDataType.getNewDataMap(this.configSetting, null);

		String tempKey = null;
		int blankLine = -1;
		int commentLine = -1;
		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.contains("}")) {
				throw new IllegalStateException("Block closed without being opened");
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
					throw new IllegalStateException("Key must not be null");
				}
				tempMap.put(tempKey, this.read(lines, blankLine, commentLine));
			} else {
				if (tempLine.contains(" = ")) {
					String[] line = tempLine.split(" = ");
					line[0] = line[0].trim();
					line[1] = line[1].trim();
					if (line[1].startsWith("[") && line[1].endsWith("]")) {
						List<String> list = Arrays.asList(line[1].substring(1, line[1].length() - 1).split(", "));
						tempMap.put(line[0], list);
					} else if (line[1].startsWith("[") && !line[1].endsWith("]")) {
						tempMap.put(line[0], this.readList(lines));
					} else {
						tempMap.put(line[0], line[1]);
					}
				} else {
					if (lines.get(1).contains("{")) {
						tempKey = tempLine;
					} else {
						throw new IllegalStateException("Key does not contain value or block");
					}
				}
			}
		}
		return tempMap;
	}

	@SuppressWarnings("DuplicatedCode")
	private Map<String, Object> read(final List<String> lines, int blankLine, int commentLine) {
		Map<String, Object> tempMap = this.fileDataType.getNewDataMap(this.configSetting, null);
		String tempKey = null;

		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);

			if (tempLine.equals("}")) {
				return tempMap;
			} else if (tempLine.contains("}")) {
				throw new IllegalStateException("Block closed without being opened");
			} else if (tempLine.isEmpty()) {
				blankLine++;
				tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
			} else if (tempLine.startsWith("#")) {
				tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
			} else if (tempLine.endsWith("{")) {
				if (!tempLine.equals("{")) {
					tempKey = tempLine.replace("{", "").trim();
				} else if (tempKey == null) {
					throw new IllegalStateException("Key must not be null");
				}
				tempMap.put(tempKey, this.read(lines, blankLine, commentLine));
			} else {
				if (tempLine.contains(" = ")) {
					String[] line = tempLine.split(" = ");
					line[0] = line[0].trim();
					line[1] = line[1].trim();
					if (line[1].startsWith("[") && line[1].endsWith("]")) {
						List<String> list = Arrays.asList(line[1].substring(1, line[1].length() - 1).split(", "));
						tempMap.put(line[0], list);
					} else if (line[1].startsWith("[")) {
						tempMap.put(line[0], this.readList(lines));
					} else {
						tempMap.put(line[0], line[1]);
					}
				} else {
					if (lines.get(1).contains("{")) {
						tempKey = tempLine;
					} else {
						throw new IllegalStateException("Key does not contain value or block");
					}
				}
			}
		}
		throw new IllegalStateException("Block does not close");
	}

	private List<String> readList(final List<String> lines) {
		List<String> localList = this.fileDataType.getNewDataList(this.configSetting, null);
		while (lines.size() > 0) {
			String tempLine = lines.get(0).trim();
			lines.remove(0);
			if (tempLine.endsWith("]")) {
				return localList;
			} else if (tempLine.startsWith("- ")) {
				localList.add(tempLine.substring(1).trim());
			} else {
				throw new IllegalStateException("List not closed properly");
			}
		}
		throw new IllegalStateException("List not closed properly");
	}


	// <Write Data>
	// <Write Data with Comments>
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

	private void topLayerWriteWithComments(final PrintWriter writer, final Map<String, Object> map, final String localKey) {
		if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
			writer.print(localKey.substring(0, localKey.lastIndexOf("{=}")));
		} else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
			writer.print("");
		} else if (map.get(localKey) instanceof Map) {
			writer.print(localKey + " " + "{");
			//noinspection unchecked
			writeWithComments((Map<String, Object>) map.get(localKey), "", writer);
		} else {
			writer.print(localKey + " = " + map.get(localKey));
		}
	}

	private void writeWithComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
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
					writeWithComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					//noinspection unchecked
					this.writeList((List<String>) map.get(localKey), indentationString + "  ", writer);
				} else {
					writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}
	// </Write Data with Comments>


	// <Write Data without Comments>
	private void initalWriteWithOutComments(final FileData fileData) {
		try (PrintWriter writer = new PrintWriter(this.file)) {
			Map<String, Object> map = fileData.toMap();
			if (!map.isEmpty()) {
				Iterator mapIterator = map.keySet().iterator();
				this.topLayerWriteWithOutComments(writer, map, mapIterator.next().toString());
				//noinspection unchecked
				mapIterator.forEachRemaining(localKey -> {
					writer.println();
					this.topLayerWriteWithOutComments(writer, map, localKey.toString());
				});
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			System.err.println("Error while writing to '" + file.getName() + "'");
			e.printStackTrace();
		}
	}

	private void topLayerWriteWithOutComments(final PrintWriter writer, final Map<String, Object> map, final String localKey) {
		if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
			if (map.get(localKey) instanceof Map) {
				writer.print(localKey + " " + "{");
				//noinspection unchecked
				this.writeWithOutComments((Map<String, Object>) map.get(localKey), "", writer);
			} else if (map.get(localKey) instanceof List) {
				writer.println("  " + localKey + " = [");
				//noinspection unchecked
				this.writeList((List<String>) map.get(localKey), "  ", writer);
			} else {
				writer.print(localKey + " = " + map.get(localKey));
			}
		}
	}

	private void writeWithOutComments(final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
		for (String localKey : map.keySet()) {
			writer.println();
			if (!localKey.startsWith("#") && map.get(localKey) != LineType.COMMENT && !localKey.startsWith("{=}emptyline") && map.get(localKey) != LineType.BLANK_LINE) {
				if (map.get(localKey) instanceof Map) {
					writer.print(indentationString + "  " + localKey + " " + "{");
					//noinspection unchecked
					this.writeWithOutComments((Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
				} else if (map.get(localKey) instanceof List) {
					writer.println(indentationString + "  " + localKey + " = [");
					//noinspection unchecked
					this.writeList((List<String>) map.get(localKey), indentationString + "  ", writer);
				} else {
					writer.print(indentationString + "  " + localKey + " = " + map.get(localKey));
				}
			}
		}
		writer.println();
		writer.print(indentationString + "}");
	}

	private void writeList(final List<String> list, final String indentationString, final PrintWriter writer) {
		for (String line : list) {
			writer.println(indentationString + "  - " + line);
		}
		writer.print(indentationString + "]");
	}

	// </Write Data without Comments>
	// </Write Data>
	@SuppressWarnings("unused")
	public enum LineType {

		VALUE,
		COMMENT,
		BLANK_LINE
	}
}