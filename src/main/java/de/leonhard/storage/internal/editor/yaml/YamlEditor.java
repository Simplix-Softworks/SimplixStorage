package de.leonhard.storage.internal.editor.yaml;

import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.YamlUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class YamlEditor {

    private final @NotNull File file;

    public @NotNull List<String> read() {
        return FileUtils.readAllLines(file);
    }

    // ----------------------------------------------------------------------------------------------------
    // Reading specific things from File
    // ----------------------------------------------------------------------------------------------------

    public @NotNull List<String> readKeys() {
        return YamlUtils.getKeys(read());
    }

    public @NotNull List<String> readComments() {
        return YamlUtils.getKeys(read());
    }

    public @NotNull List<String> readHeader() {
        return YamlUtils.getHeaderFromLines(read());
    }

    public @NotNull List<String> readFooter() {
        return YamlUtils.getFooterFromLines(read());
    }

    public @NotNull List<String> readPureComments() {
        return YamlUtils.getPureCommentsFromLines(read());
    }

    public @NotNull List<String> readWithoutHeaderAndFooter() {
        return YamlUtils.getLinesWithoutFooterAndHeaderFromLines(read());
    }

    // ----------------------------------------------------------------------------------------------------
    // Writing specific things from File
    // ----------------------------------------------------------------------------------------------------
    public void write(final @NotNull List<String> lines) {
        FileUtils.write(file, lines);
    }

    public void setHeader(final @NotNull List<String> header) {
        val lines = read();

        // Remove old header
        lines.removeAll(readHeader());

        // Adding new header in front
        for (int i = 0; i < header.size(); i++) {
            val toAdd = header.get(i);
            lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
        }

        // Write to file
        write(lines);
    }

    public void addHeader(final @NotNull List<String> header) {
        val lines = read();

        for (int i = 0; i < header.size(); i++) {
            val toAdd = header.get(i);
            lines.add(i, toAdd.startsWith("#") ? toAdd : "#" + toAdd);
        }

        // Write to file
        write(lines);
    }
}
