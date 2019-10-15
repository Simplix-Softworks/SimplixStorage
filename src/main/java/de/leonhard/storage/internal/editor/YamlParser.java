package de.leonhard.storage.internal.editor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlParser {

    final private YamlEditor yamlEditor;
    final private File file;


    public YamlParser(final YamlEditor yamlEditor) {
        this.yamlEditor = yamlEditor;
        this.file = yamlEditor.getFile();
    }


    /**
     * Method to assign a comment to a key
     *
     * @return
     */

    private Map<String, List<String>> assignCommentsToKey() throws IOException {
        return assignCommentsToKey(yamlEditor.read());
    }

    public List<String> parseComments(final List<String> comments, final List<String> updated) {
        final List<String> keys;
        final Map<String, List<String>> parsed;
        try {
            keys = yamlEditor.readKeys();
            parsed = assignCommentsToKey(comments);
        } catch (final IOException e) {
            System.err.println("Exception while reading keys from '" + file.getName() + "'");
            e.printStackTrace();
            return new ArrayList<>();
        }

        for (final String key : parsed.keySet()) {
            int i = 0;
            for (final String line : parsed.get(key)) {
                if (updated.contains(key + " ")) {
                    updated.add(updated.indexOf(key + " ") + i, line);
                } else if (updated.contains(" " + key)) {
                    updated.add(updated.indexOf(" " + key) + i, line);
                }
            }
        }
        return updated;
    }


    private boolean contains(final List<String> list, final String toFind) {
        for (final String toCheck : list) {
            if (toCheck.startsWith("#"))
                continue;
            if (toCheck.equals(toFind)) {
                System.out.println("The value '" + toCheck + "' is equal to '" + toFind + "'.");
                return true;
            } else {
                System.out.println("The value '" + toCheck + "' is not equal to '" + toFind + "'.");
            }
        }
        return false;
    }

    private Map<String, List<String>> assignCommentsToKey(final List<String> fileLines) {

        List<String> storage = new ArrayList<>();
        final List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
        final Map<String, List<String>> result = new HashMap<>();

        //Loop over the remaining lines
        int i = 0;
        Collections.reverse(lines);//Reverse -> Should start from the end
        for (final String line : lines) {
            if (line.replaceAll("\\s+", "").startsWith("#") || line.isEmpty()) { //Replacing the whitespaces
                storage.add(line);
                continue;
            }
            result.put(line, storage);
            storage = new ArrayList<>();
        }

        //Removing keys without comments

        final List<String> keysToRemove = new ArrayList<>();
        for (final String line : result.keySet()) {
            if (result.get(line).equals(new ArrayList<>()))
                keysToRemove.add(line);
        }

        for (final String key : keysToRemove)
            result.remove(key);

        return result;

    }
}