package de.leonhard.storage.lightningstorage.utils;

import de.leonhard.storage.lightningstorage.editor.YamlEditor;
import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class YamlUtils {

	final private YamlEditor yamlEditor;


	public YamlUtils(final YamlEditor yamlEditor) {
		this.yamlEditor = yamlEditor;
	}


	@SuppressWarnings("UnusedAssignment")
	public List<String> parseComments(@NotNull final List<String> comments, @NotNull final List<String> updated) {
		final List<String> keys;
		final Map<String, List<String>> parsed;
		try {
			keys = yamlEditor.readKeys();
			parsed = assignCommentsToKey(comments);
		} catch (IOException e) {
			System.err.println("Exception while reading keys from '" + yamlEditor.getFile().getName() + "'");
			e.printStackTrace();
			return new ArrayList<>();
		}

		for (final String key : parsed.keySet()) {
			int i = 0;
			for (final String line : parsed.get(key)) {
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

	private Map<String, List<String>> assignCommentsToKey(@NotNull final List<String> fileLines) {
		List<String> storage = new ArrayList<>();
		final List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
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

	/**
	 * Method to assign a comment to a key
	 *
	 * @return Nested Map with Comments assigned to the corresponding keys
	 * @throws IOException if file could not be read
	 */
	private Map<String, List<String>> assignCommentsToKey() throws IOException {
		return assignCommentsToKey(yamlEditor.read());
	}
}