package de.leonhard.storage.internal.editor.yaml;

import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class YamlEditor {

  private final File file;

  public List<String> read() {
    return FileUtils.readAllLines(file);
  }

  // ----------------------------------------------------------------------------------------------------
  // Reading specific things from File
  // ----------------------------------------------------------------------------------------------------

  public List<String> readKeys() {
    return YamlStringEditor.getKeys(read());
  }

  public List<String> readComments() {
    return YamlStringEditor.getKeys(read());
  }

  public List<String> readHeader() {
    return YamlStringEditor.getHeaderFromLines(read());
  }

  public List<String> readFooter() {
    return YamlStringEditor.getFooterFromLines(read());
  }

  public List<String> readPureComments() {
    return YamlStringEditor.getPureCommentsFromLines(read());
  }

  public List<String> readWithoutHeaderAndFooter() {
    return YamlStringEditor.getLinesWithoutFooterAndHeaderFromLines(read());
  }

  // ----------------------------------------------------------------------------------------------------
  // Writing specific things from File
  // ----------------------------------------------------------------------------------------------------
  public void write(final List<String> lines) {
    FileUtils.write(file, lines);
  }

  public void setHeader(final List<String> header) {
    final List<String> lines = read();

    // Remove old header
    lines.removeAll(readHeader());

    // Adding new header in front
    for (int i = 0; i < header.size(); i++) {
      final String toAdd = header.get(i);
      lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
    }

    // Write to file
    write(lines);
  }

  public void addHeader(final List<String> header) {
    final List<String> lines = read();
    for (int i = 0; i < header.size(); i++) {
      final String toAdd = header.get(i);
      lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
    }

    // Write to file
    write(lines);
  }
}
