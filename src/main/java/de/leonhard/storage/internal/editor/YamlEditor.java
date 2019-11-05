package de.leonhard.storage.internal.editor;

import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"unused", "WeakerAccess"})
public class YamlEditor {

	private final File file;


	protected YamlEditor(final File file) {
		this.file = file;
	}


	public final File getFile() {
		return file;
	}

	public List<String> readComments() throws IOException {
		return getCommentsFromLines(read());
	}

	public List<String> read() throws IOException {
		return Files.readAllLines(file.toPath());
	}

	public static List<String> getCommentsFromLines(final @NotNull List<String> lines) {
		final List<String> result = new ArrayList<>();
		for (final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	public List<String> readFooter() throws IOException {
		return getFooterFromLines(read());
	}

	public static List<String> getFooterFromLines(final @NotNull List<String> lines) {
		Objects.checkNull(lines, "Lines must not be null");

		final List<String> result = new ArrayList<>();
		Collections.reverse(lines);
		for (final String line : lines) {
			if (!line.startsWith("#")) {
				Collections.reverse(result);
				return result;
			} else {
				result.add(line);
			}
		}
		Collections.reverse(result);
		return result;
	}

	public List<String> readHeader() throws IOException {
		return getHeaderFromLines(read());
	}

	public static List<String> getHeaderFromLines(final @NotNull List<String> lines) {
		final List<String> result = new ArrayList<>();
		for (final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.startsWith("#")) {
				return result;
			} else {
				result.add(line);
			}
		}
		return result;
	}

	public List<String> readKeys() throws IOException {
		return getKeys(read());
	}

	public static List<String> getKeys(final @NotNull List<String> lines) {
		final List<String> result = new ArrayList<>();
		for (final String line : Objects.notNull(lines, "Lines must not be null")) {
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
	public static List<String> getPureCommentsFromLines(final @NotNull List<String> lines) {
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

	public static List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines) {
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	public void write(final @NotNull List<String> lines) throws IOException {
		@Cleanup PrintWriter writer = new PrintWriter(new FileWriter(this.file));
		Iterator tempIterator = lines.iterator();
		writer.print(tempIterator.next());
		//noinspection unchecked
		tempIterator.forEachRemaining(line -> {
			writer.println();
			writer.print(line);
		});
	}
}