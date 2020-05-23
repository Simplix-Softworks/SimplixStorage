package de.leonhard.storage.internal.editor.yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public final class YamlParser {

  private static final String FOOTER = "FOOTER";

  private final YamlEditor yamlEditor;

  public List<String> parseLines(
      final List<String> comments,
      final List<String> updated) {
    final List<String> out = new ArrayList<>();
    final Map<String, List<String>> parsed = assignCommentsToKey(comments);

    for (final String line : updated) {
      final val rawList = getKeyAndRemove(line, parsed);
      if (rawList == null || rawList.isEmpty()) {

        out.add(line);
        continue;
      }

      Collections.reverse(rawList);
      out.addAll(rawList);
      if (!line.equals(FOOTER)) {
        out.add(line);
      }
    }

    return out;
  }

  private List<String> getKeyAndRemove(String key, final Map<String, List<String>> data) {
    key = key.split(":")[0];
    for (final val entry : data.entrySet()) {
      if (key.equals(entry.getKey().split(":")[0])) {
        data.remove(entry.getKey());
        return entry.getValue();
      }
    }

    return new ArrayList<>();
  }

  public Map<String, List<String>> assignCommentsToKey() {
    return assignCommentsToKey(yamlEditor.read());
  }

  /**
   * Method to map the #-comments in your YAML-File to the according key
   * @param lines Initial lines to read
   * @return Mapped comments (Key, comments)
   */
  public Map<String, List<String>> assignCommentsToKey(final List<String> lines) {
    final Map<String, List<String>> out = new HashMap<>();

    String currentKey = FOOTER;
    for (int i = lines.size() - 1; i >= 0; i--) {
      final String line = lines.get(i);
      if (line.trim().startsWith("#") || line.isEmpty()) {
        final List<String> storage = out.get(currentKey.split(":")[0]);

        if (storage == null) {
          out.put(currentKey.split(":")[0],
              new ArrayList<>(Collections.singletonList(line)));
        } else {
          storage.add(line);
        }
      } else {
        currentKey = line;
      }
    }
    return out;
  }
}
