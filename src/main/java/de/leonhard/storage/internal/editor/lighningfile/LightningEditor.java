package de.leonhard.storage.internal.editor.lighningfile;

import de.leonhard.storage.internal.exception.LightningFileException;
import de.leonhard.storage.internal.settings.ConfigSettings;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

/** Class for parsing a Lightning-Type File Written by Zeanon */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class LightningEditor {
  private final File file;

  /**
   * Write the given Data to a File.
   *
   * @param map A HashMap containing the Data to be written.
   * @param configSetting the ConfigSetting to be used.
   */
  public void writeData(final Map<String, Object> map, final ConfigSettings configSetting) {
    if (ConfigSettings.PRESERVE_COMMENTS == configSetting) {
      initialWriteWithComments(file, map);
      return;
    }
    initialWriteWithOutComments(file, map);
  }

  /**
   * Read the Data of a File.
   *
   * @return a Map containing the Data of the File.
   */
  public Map<String, Object> readData() {
    try {
      final List<String> lines = Files.readAllLines(file.toPath());
      final Map<String, Object> tempMap = new HashMap<>();

      String tempKey = null;
      int blankLine = -1;
      int commentLine = -1;
      while (lines.size() > 0) {
        final String tempLine = lines.get(0).trim();
        lines.remove(0);

        if (tempLine.contains("}")) {
          throw new LightningFileException(
              "Error at '" + file.getAbsolutePath() + "' -> Block closed without being opened");
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
            throw new LightningFileException(
                "Error at '" + file.getAbsolutePath() + "' -> Key must not be null");
          }
          tempMap.put(tempKey, internalRead(file.getAbsolutePath(), lines, blankLine, commentLine));
        } else {
          if (tempLine.contains("=")) {
            final String[] line = tempLine.split("=");
            line[0] = line[0].trim();
            line[1] = line[1].trim();
            if (line[1].startsWith("[")) {
              if (line[1].endsWith("]")) {
                final String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
                final List<String> list = new ArrayList<>();
                for (final String value : listArray) {
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
              throw new LightningFileException(
                  "Error at '"
                      + file.getAbsolutePath()
                      + "' -> '"
                      + tempLine
                      + "' does not contain value or subblock");
            }
          }
        }
      }
      return tempMap;
    } catch (final IOException | ArrayIndexOutOfBoundsException e) {
      System.err.println("Error while reading '" + file.getAbsolutePath() + "'");
      e.printStackTrace();
      throw new LightningFileException();
    }
  }

  // <Read Data>
  private Map<String, Object> internalRead(
      final String filePath, final List<String> lines, int blankLine, final int commentLine)
      throws ArrayIndexOutOfBoundsException {
    final Map<String, Object> tempMap = new HashMap<>();
    String tempKey = null;

    while (lines.size() > 0) {
      final String tempLine = lines.get(0).trim();
      lines.remove(0);

      if (tempLine.equals("}")) {
        return tempMap;
      } else if (tempLine.contains("}")) {
        throw new LightningFileException(
            "Error at '" + filePath + "' -> Block closed without being opened");
      } else if (tempLine.isEmpty()) {
        blankLine++;
        tempMap.put("{=}emptyline" + blankLine, LineType.BLANK_LINE);
      } else if (tempLine.startsWith("#")) {
        tempMap.put(tempLine + "{=}" + commentLine, LineType.COMMENT);
      } else if (tempLine.endsWith("{")) {
        if (!tempLine.equals("{")) {
          tempKey = tempLine.replace("{", "").trim();
        } else if (tempKey == null) {
          throw new LightningFileException("Error at '" + filePath + "' -> Key must not be null");
        }
        tempMap.put(tempKey, internalRead(filePath, lines, blankLine, commentLine));
      } else {
        if (tempLine.contains("=")) {
          final String[] line = tempLine.split("=");
          line[0] = line[0].trim();
          line[1] = line[1].trim();
          if (line[1].startsWith("[")) {
            if (line[1].endsWith("]")) {
              final String[] listArray = line[1].substring(1, line[1].length() - 1).split(",");
              final List<String> list = new ArrayList<>();
              for (final String value : listArray) {
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
            throw new LightningFileException(
                "Error at '"
                    + filePath
                    + "' -> '"
                    + tempLine
                    + "' does not contain value or subblock");
          }
        }
      }
    }
    throw new LightningFileException("Error at '" + filePath + "' -> Block does not close");
  }

  private List<String> readList(final String filePath, final List<String> lines) {
    final List<String> localList = new ArrayList<>();
    while (lines.size() > 0) {
      final String tempLine = lines.get(0).trim();
      lines.remove(0);
      if (tempLine.startsWith("-")) {
        localList.add(tempLine.substring(1).trim());
      } else if (tempLine.endsWith("]")) {
        return localList;
      } else {
        throw new LightningFileException("Error at '" + filePath + "' -> List not closed properly");
      }
    }
    throw new LightningFileException("Error at '" + filePath + "' -> List not closed properly");
  }
  // </Read Data>

  // <Write Data>
  // <Write Data with Comments>
  private void initialWriteWithComments(final File file, final Map<String, Object> map) {
    try (final PrintWriter writer = new PrintWriter(file)) {
      if (!map.isEmpty()) {
        final Iterator mapIterator = map.keySet().iterator();
        topLayerWriteWithComments(writer, map, (String) mapIterator.next());
        mapIterator.forEachRemaining(
            localKey -> {
              writer.println();
              topLayerWriteWithComments(writer, map, (String) localKey);
            });
      }
      writer.flush();
    } catch (final FileNotFoundException e) {
      System.err.println("Could not write to '" + file.getAbsolutePath() + "'");
      e.printStackTrace();
      throw new LightningFileException();
    }
  }

  private void topLayerWriteWithComments(
      final PrintWriter writer, final Map<String, Object> map, final String localKey) {
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

  private void internalWriteWithComments(
      final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
    for (final String localKey : map.keySet()) {
      writer.println();
      if (localKey.startsWith("#") && map.get(localKey) == LineType.COMMENT) {
        writer.print(indentationString + "  " + localKey.substring(0, localKey.lastIndexOf("{=}")));
      } else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == LineType.BLANK_LINE) {
        writer.print("");
      } else {
        if (map.get(localKey) instanceof Map) {
          writer.print(indentationString + "  " + localKey + " " + "{");
          internalWriteWithComments(
              (Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
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
  private void initialWriteWithOutComments(final File file, final Map<String, Object> map) {
    try (final PrintWriter writer = new PrintWriter(file)) {
      if (!map.isEmpty()) {
        final Iterator mapIterator = map.keySet().iterator();
        topLayerWriteWithOutComments(writer, map, mapIterator.next().toString());
        mapIterator.forEachRemaining(
            localKey -> {
              writer.println();
              topLayerWriteWithOutComments(writer, map, localKey.toString());
            });
      }
      writer.flush();
    } catch (final FileNotFoundException e) {
      System.err.println("Could not write to '" + file.getAbsolutePath() + "'");
      e.printStackTrace();
    }
  }

  private void topLayerWriteWithOutComments(
      final PrintWriter writer, final Map<String, Object> map, final String localKey) {
    if (!localKey.startsWith("#")
        && map.get(localKey) != LineType.COMMENT
        && !localKey.startsWith("{=}emptyline")
        && map.get(localKey) != LineType.BLANK_LINE) {
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

  private void internalWriteWithoutComments(
      final Map<String, Object> map, final String indentationString, final PrintWriter writer) {
    for (final String localKey : map.keySet()) {
      writer.println();
      if (!localKey.startsWith("#")
          && map.get(localKey) != LineType.COMMENT
          && !localKey.startsWith("{=}emptyline")
          && map.get(localKey) != LineType.BLANK_LINE) {
        if (map.get(localKey) instanceof Map) {
          writer.print(indentationString + "  " + localKey + " " + "{");
          internalWriteWithoutComments(
              (Map<String, Object>) map.get(localKey), indentationString + "  ", writer);
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

  private void writeList(
      final List<String> list, final String indentationString, final PrintWriter writer) {
    for (final String line : list) {
      writer.println(indentationString + "  - " + line);
    }
    writer.print(indentationString + "]");
  }
  // </Write Data>

  private enum LineType {
    COMMENT,
    BLANK_LINE
  }
}
