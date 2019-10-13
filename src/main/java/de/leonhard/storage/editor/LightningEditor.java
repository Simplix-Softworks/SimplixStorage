package de.leonhard.storage.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked", "ConstantConditions"})
public class LightningEditor {

    private final File file;

    public LightningEditor(final File file) {
        this.file = file;
    }

    public List<String> readLines() throws IOException {
        final byte[] bytes = Files.readAllBytes(file.toPath());
        return Arrays.asList(new String(bytes).split("\n"));
    }

    public Map<String, Object> readData() throws IOException {
        final byte[] bytes = Files.readAllBytes(file.toPath());
        final Object obj = new String(bytes).replace("\n", "");
        return (Map<String, Object>) obj;
    }
}