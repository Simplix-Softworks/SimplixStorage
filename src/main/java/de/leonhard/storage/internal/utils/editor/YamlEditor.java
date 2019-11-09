package de.leonhard.storage.internal.utils.editor;

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
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlEditor {

	public static List<String> readComments(final @NotNull File file) throws IOException {
		return getCommentsFromLines(read(file));
	}

	public static List<String> read(final @NotNull File file) throws IOException {
		return Files.readAllLines(file.toPath());
	}

	public static List<String> readFooter(final @NotNull File file) throws IOException {
		return getFooterFromLines(read(file));
	}

	public static List<String> readHeader(final @NotNull File file) throws IOException {
		return getHeaderFromLines(read(file));
	}

	public static List<String> readKeys(final @NotNull File file) throws IOException {
		return getKeys(read(file));
	}

	public static List<String> readPureComments(final @NotNull File file) throws IOException {
		return getPureCommentsFromLines(read(file));
	}

	public static List<String> readWithoutHeaderAndFooter(final @NotNull File file) throws IOException {
		return getLinesWithoutFooterAndHeaderFromLines(read(file));
	}

	public static void write(final @NotNull File file, final @NotNull List<String> lines) throws IOException {
		@Cleanup PrintWriter writer = new PrintWriter(new FileWriter(file));
		Iterator tempIterator = lines.iterator();
		writer.print(tempIterator.next());
		//noinspection unchecked
		tempIterator.forEachRemaining(line -> {
			writer.println();
			writer.print(line);
		});
	}

	public static List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines) {
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		lines.removeAll(header);
		lines.removeAll(footer);

		return lines;
	}

	private static List<String> getCommentsFromLines(final @NotNull List<String> lines) {
		final List<String> result = new ArrayList<>();
		for (final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (line.startsWith("#")) {
				result.add(line);
			}
		}
		return result;
	}

	private static List<String> getFooterFromLines(final @NotNull List<String> lines) {
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

	private static List<String> getHeaderFromLines(final @NotNull List<String> lines) {
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

	private static List<String> getKeys(final @NotNull List<String> lines) {
		final List<String> result = new ArrayList<>();
		for (final String line : Objects.notNull(lines, "Lines must not be null")) {
			if (!line.replaceAll("\\s+", "").startsWith("#")) {
				result.add(line);
			}
		}

		return result;
	}

	/**
	 * @return List of comments that don't belong to header or footer
	 */
	private static List<String> getPureCommentsFromLines(final @NotNull List<String> lines) {
		final List<String> comments = getCommentsFromLines(lines);
		final List<String> header = getHeaderFromLines(lines);
		final List<String> footer = getFooterFromLines(lines);

		comments.removeAll(header);
		comments.removeAll(footer);

		return comments;
	}
}