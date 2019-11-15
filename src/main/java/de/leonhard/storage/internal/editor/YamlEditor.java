package de.leonhard.storage.internal.editor;

import de.leonhard.storage.utils.FileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused", "Duplicates", "WeakerAccess"})
@Getter
@RequiredArgsConstructor
public class YamlEditor {

    private final File file;


    public List<String> read() {
        return FileUtils.readAllLines(file);
    }

    // ----------------------------------------------------------------------------------------------------
    // Reading specific things from File
    // ----------------------------------------------------------------------------------------------------

    public List<String> readKeys() {
        return getKeys(read());
    }

    public List<String> readComments() {
        return getCommentsFromLines(read());
    }

    public List<String> readHeader() {
        return getHeaderFromLines(read());
    }

    public List<String> readFooter() {
        return getFooterFromLines(read());
    }

    public List<String> readPureComments() {
        return getPureCommentsFromLines(read());
    }

    public List<String> readWithoutHeaderAndFooter() {
        return getLinesWithoutFooterAndHeaderFromLines(read());
    }

    public void write(final List<String> lines) {
        FileUtils.write(file, lines);
    }


    private List<String> getCommentsFromLines(final List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (final String line : lines) {
            if (line.startsWith("#")) {
                result.add(line);
            }
        }
        return result;
    }

    private List<String> getFooterFromLines(final List<String> lines) {
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

    private List<String> getHeaderFromLines(final List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (final String line : lines) {
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
    private List<String> getPureCommentsFromLines(final List<String> lines) {
        final List<String> comments = getCommentsFromLines(lines);
        final List<String> header = getHeaderFromLines(lines);
        final List<String> footer = getFooterFromLines(lines);

        comments.removeAll(header);
        comments.removeAll(footer);

        return comments;
    }

    private List<String> getLinesWithoutFooterAndHeaderFromLines(final List<String> lines) {
        final List<String> header = getHeaderFromLines(lines);
        final List<String> footer = getFooterFromLines(lines);

        lines.removeAll(header);
        lines.removeAll(footer);

        return lines;
    }

    private List<String> getKeys(final List<String> lines) {
        final List<String> result = new ArrayList<>();

        for (final String line : lines) {
            if (!line.replaceAll("\\s+", "").startsWith("#")) {
                result.add(line);
            }
        }
        return result;
    }
}