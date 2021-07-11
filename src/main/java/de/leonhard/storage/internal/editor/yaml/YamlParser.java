package de.leonhard.storage.internal.editor.yaml;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiredArgsConstructor
public final class YamlParser {

    private static final String FOOTER = "FOOTER";

    private final @NotNull YamlEditor yamlEditor;

    public @NotNull List<String> parseLines(final @NotNull List<String> comments, final @NotNull List<String> updated) {
        final List<String> out = new ArrayList<>();
        val parsed = assignCommentsToKey(comments);

        for (val line : updated) {
            val rawList = getKeyAndRemove(line, parsed);

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

    private List<String> getKeyAndRemove(String key, final @NotNull Map<String, List<String>> data) {
        key = key.split(":")[0];
        for (val entry : data.entrySet()) {
            val entryKey = entry.getKey().split(":")[0];

            // using substring since indentation might differ a bit
            if (key.equals(entryKey) || key.substring(1).equals(entryKey)) {
                data.remove(entry.getKey());
                return entry.getValue();
            }
        }

        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public @NotNull Map<String, List<String>> assignCommentsToKey() {
        return assignCommentsToKey(this.yamlEditor.read());
    }

    /**
     * Method to map the #-comments in your YAML-File to the according key
     *
     * @param lines Initial lines to read
     * @return Mapped comments (Key, comments)
     */
    public @NotNull Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> lines) {
        final Map<String, List<String>> out = new HashMap<>();

        String currentKey = FOOTER;
        for (int i = lines.size() - 1; i >= 0; i--) {
            val line = lines.get(i);
            if (!line.trim().startsWith("#") && !line.isEmpty()) {
                currentKey = line;
            } else {
                val storage = out.get(currentKey.split(":")[0]);
                if (storage == null) {
                    out.put(currentKey.split(":")[0], new ArrayList<>(Collections.singletonList(line)));
                } else if (!storage.contains(line)) {
                    storage.add(line);
                }
            }
        }
        return out;
    }
}
