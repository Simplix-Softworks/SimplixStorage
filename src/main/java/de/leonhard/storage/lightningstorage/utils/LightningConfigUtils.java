package de.leonhard.storage.lightningstorage.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"DuplicatedCode", "WeakerAccess", "unused"})
public class LightningConfigUtils {

	private final File file;

	public LightningConfigUtils(@NotNull final File file) {
		this.file = file;
	}

	public List<String> readHeader() throws IOException {
		return getHeaderFromLines(read());
	}

	public List<String> read() throws IOException {
		return Files.readAllLines(file.toPath());
	}

	public static List<String> getHeaderFromLines(@NotNull final List<String> lines) {
		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (!line.startsWith("#")) {
				return result;
			}
			result.add(line);
		}
		return result;
	}

	public List<String> readFooter() throws IOException {
		return getFooterFromLines(read());
	}

	public static List<String> getFooterFromLines(@NotNull final List<String> lines) {
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

	public List<String> readPureComments() throws IOException {
		return getPureCommentsFromLines(read());
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	public static List<String> getPureCommentsFromLines(@NotNull final List<String> lines) {
		final List<String> comments = getCommentsFromLines(lines);
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}

	public static List<String> getCommentsFromLines(@NotNull final List<String> lines) {
		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	public void write(@NotNull final List<String> lines) throws IOException {
		final FileWriter writer = new FileWriter(file);
		for (final String str : lines) {
			writer.write(str + "\n");
		}
		writer.close();
	}

	public List<String> readComments() throws IOException {
		return getCommentsFromLines(read());
	}
}