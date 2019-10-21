package de.leonhard.storage.lightningstorage.editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SuppressWarnings({"unused", "Duplicates", "WeakerAccess"})
public class YamlEditor {

	private final File file;


	public YamlEditor(final File file) {
		this.file = file;
	}


	public final File getFile() {
		return file;
	}

	public List<String> readComments() throws IOException {
		return getCommentsFromLines(read());
	}

	public static List<String> getCommentsFromLines(final List<String> lines) {
		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	public List<String> read() throws IOException {
		return Files.readAllLines(file.toPath());
	}

	public List<String> readFooter() throws IOException {
		return getFooterFromLines(read());
	}

	public static List<String> getFooterFromLines(final List<String> lines) {
		final List<String> result = new ArrayList<>();
		Collections.reverse(lines);
		for (final String line : lines) {
			if (!line.startsWith("#")) {
				Collections.reverse(result);
				return result;
			}
			result.add(line);
		}
		Collections.reverse(result);
		return result;
	}

	public List<String> readHeader() throws IOException {
		return getHeaderFromLines(read());
	}

	public static List<String> getHeaderFromLines(final List<String> lines) {
		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (!line.startsWith("#")) {
				return result;
			}
			result.add(line);
		}
		return result;
	}

	public List<String> readKeys() throws IOException {
		return getKeys(read());
	}

	public static List<String> getKeys(final List<String> lines) {
		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}

		return result;
	}

	public List<String> readPureComments() throws IOException {
		return getPureCommentsFromLines(read());
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	public static List<String> getPureCommentsFromLines(final List<String> lines) {
		final List<String> comments = getCommentsFromLines(lines);
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}

	public List<String> readWithoutHeaderAndFooter() throws IOException {
		return getLinesWithoutFooterAndHeaderFromLines(read());
	}

	public static List<String> getLinesWithoutFooterAndHeaderFromLines(final List<String> lines) {
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	public void write(final List<String> lines) throws IOException {
		final PrintWriter writer = new PrintWriter(new FileWriter(file));
		for (final String str : lines) {
			writer.println(str);
		}
		writer.close();
	}
}