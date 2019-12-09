package de.leonhard.storage.internal.editor.yaml;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to work with the parts of the lines of a YAML-file
 */
@UtilityClass
public class YamlStringEditor {
	public List<String> getCommentsFromLines(List<String> lines) {
		List<String> result = new ArrayList<>();

		for (String line : lines) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	public List<String> getFooterFromLines(List<String> lines) {
		List<String> result = new ArrayList<>();
		Collections.reverse(lines);
		for (String line : lines) {
			if (!line.startsWith("#")) {
				Collections.reverse(result);
				return result;
			}
			result.add(line);
		}
		Collections.reverse(result);
		return result;
	}

	public List<String> getHeaderFromLines(List<String> lines) {
		List<String> result = new ArrayList<>();

		for (String line : lines) {
			if (!line.startsWith("#")) {
				return result;
			}
			result.add(line);
		}
		return result;
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	public List<String> getPureCommentsFromLines(List<String> lines) {
		List<String> comments = getCommentsFromLines(lines);
		List<String> header = getHeaderFromLines(lines);
		List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}

	public List<String> getLinesWithoutFooterAndHeaderFromLines(List<String> lines) {
		List<String> header = getHeaderFromLines(lines);
		List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	public List<String> getKeys(List<String> lines) {
		List<String> result = new ArrayList<>();

		for (String line : lines) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}
}
