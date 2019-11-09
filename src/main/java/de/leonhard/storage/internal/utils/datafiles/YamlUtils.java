package de.leonhard.storage.internal.utils.datafiles;

import de.leonhard.storage.internal.utils.editor.YamlEditor;
import java.io.File;
import java.io.IOException;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Adds several utility Methods for Yaml-Files
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlUtils {


	@SuppressWarnings("UnusedAssignment")
	public static List<String> parseComments(final @NotNull File file, final @NotNull List<String> comments, final @NotNull List<String> updated) {
		final List<String> keys;
		final Map<String, List<String>> parsed;
		try {
			keys = YamlEditor.readKeys(file);
			parsed = assignCommentsToKey(comments);
		} catch (IOException e) {
			System.err.println("Error while reading keys from '" + file.getAbsolutePath() + "'");
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

	private static Map<String, List<String>> assignCommentsToKey(final @NotNull List<String> fileLines) {
		List<String> storage = new ArrayList<>();
		final List<String> lines = YamlEditor.getLinesWithoutFooterAndHeaderFromLines(fileLines);
		final Map<String, List<String>> result = new HashMap<>();

		// Loop over the remaining lines
		Collections.reverse(lines);// Reverse -> Should start from the end
		for (final String line : lines) {
			if (line.trim().startsWith("#") || line.isEmpty()) { // Replacing the whitespaces
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
	 * @throws IOException if File could not be read
	 */
	private static Map<String, List<String>> assignCommentsToKey(final @NotNull File file) throws IOException {
		return assignCommentsToKey(YamlEditor.read(file));
	}
}