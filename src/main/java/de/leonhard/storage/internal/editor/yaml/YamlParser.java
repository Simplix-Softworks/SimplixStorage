package de.leonhard.storage.internal.editor.yaml;

import lombok.RequiredArgsConstructor;

import java.util.*;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class YamlParser {
    private final YamlEditor yamlEditor;

    /**
     * Method to assign a comment to a key
     */
    private Map<String, List<String>> assignCommentsToKey() {
        return assignCommentsToKey(yamlEditor.read());
    }

    public List<String> parseComments(final List<String> comments, final List<String> updated) {
        final List<String> keys;
        final Map<String, List<String>> parsed;
        keys = yamlEditor.readKeys();
        parsed = assignCommentsToKey(comments);

        for (final String key : parsed.keySet()) {
            int i = 0;
            for (final String line : parsed.get(key)) {
                if (line.isEmpty())
                    continue;
                if (updated.contains(key + " ")) {
                    updated.add(updated.indexOf(key + " ") + i, line);
                    continue;
                }
                if (updated.contains(" " + key)) {
                    updated.add(updated.indexOf(" " + key) + i, line);
                }

            }
        }
        return updated;
    }

    private Map<String, List<String>> assignCommentsToKey(final List<String> fileLines) {
        List<String> storage = new ArrayList<>();
        final List<String> lines = YamlStringEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
        final Map<String, List<String>> result = new HashMap<>();

        // Loop over the remaining lines
        Collections.reverse(lines);// Reverse -> Should start from the end
        for (final String line : lines) {
            if (line.replaceAll("\\s+", "").startsWith("#") || line.isEmpty()) { // Replacing the whitespaces
                storage.add(line);
                continue;
            }
            result.put(line, storage);
            storage = new ArrayList<>();
        }

        // Removing keys without comments

        final List<String> keysToRemove = new ArrayList<>();
        for (final String line : result.keySet()) {
            if (result.get(line).equals(new ArrayList<>())) {
                keysToRemove.add(line);
            }
        }

        for (final String key : keysToRemove) {
            result.remove(key);
        }

        return result;
    }
}