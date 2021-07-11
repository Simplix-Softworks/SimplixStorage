package de.leonhard.storage.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to work with the parts of the lines of a YAML-file
 */
@UtilityClass
public class YamlUtils {

    public @NotNull List<String> getCommentsFromLines(final @NotNull List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (val line : lines) {
            if (line.startsWith("#")) {
                result.add(line);
            }
        }

        return result;
    }

    public @NotNull List<String> getFooterFromLines(final @NotNull List<String> lines) {
        final List<String> result = new ArrayList<>();

        Collections.reverse(lines);

        for (val line : lines) {
            if (!line.startsWith("#")) {
                Collections.reverse(result);
                return result;
            }

            result.add(line);
        }

        Collections.reverse(result);
        return result;
    }

    public @NotNull List<String> getHeaderFromLines(final @NotNull List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (val line : lines) {
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
    public @NotNull List<String> getPureCommentsFromLines(final @NotNull List<String> lines) {
        val comments = getCommentsFromLines(lines);
        val header = getHeaderFromLines(lines);
        val footer = getFooterFromLines(lines);

        comments.removeAll(header);
        comments.removeAll(footer);

        return comments;
    }

    public @NotNull List<String> getLinesWithoutFooterAndHeaderFromLines(final @NotNull List<String> lines) {
        val header = getHeaderFromLines(lines);
        val footer = getFooterFromLines(lines);

        lines.removeAll(header);
        lines.removeAll(footer);

        return lines;
    }

    public @NotNull List<String> getKeys(final @NotNull List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (val line : lines) {
            if (!line.trim().startsWith("#")) {
                result.add(line);
            }
        }

        return result;
    }
}
