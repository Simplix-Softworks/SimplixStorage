package de.leonhard.storage;

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

    public Map<String, List<String>> assignCommentsToKey() throws IOException {
        return assignCommentsToKey(yamlEditor.read());
    }

    public static Map<String, List<String>> assignCommentsToKey(final List<String> fileLines) {

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
