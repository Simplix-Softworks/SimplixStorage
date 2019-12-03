package de.leonhard.storage.internal.editor.yaml;

import lombok.RequiredArgsConstructor;

import java.util.*;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public final class YamlParser {
	private final YamlEditor yamlEditor;


	public List<String> parseComments(List<String> comments, List<String> updated) {
		List<String> keys;
		Map<String, List<String>> parsed = assignCommentsToKey(comments);

		for (String key : parsed.keySet()) {
			int i = 0;
			for (String line : parsed.get(key)) {
				if (line.isEmpty()) {
					continue;
				}
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


	private Map<String, List<String>> assignCommentsToKey() {
		return assignCommentsToKey(yamlEditor.read());
	}

	//Method to assign the comments in a YAML to their appropriate keys
	private Map<String, List<String>> assignCommentsToKey(List<String> fileLines) {
		List<String> storage = new ArrayList<>();
		List<String> lines = YamlStringEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
		Map<String, List<String>> result = new HashMap<>();

		// Loop over the remaining lines
		Collections.reverse(lines);// Reverse -> Should start from the end
		for (String line : lines) {
			if (line.replaceAll("\\s+", "").startsWith("#") || line.isEmpty()) { // Replacing the whitespaces
				storage.add(line);
				continue;
			}
			result.put(line, storage);
			storage = new ArrayList<>();
		}

		// Removing keys without comments
		List<String> keysToRemove = new ArrayList<>();
		for (String line : result.keySet()) {
			if (result.get(line).equals(new ArrayList<>())) {
				keysToRemove.add(line);
			}
		}

		for (String key : keysToRemove) {
			result.remove(key);
		}

		return result;
	}
}