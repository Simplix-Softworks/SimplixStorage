package de.leonhard.storage.internal.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static boolean hasChanged(final File file, final long timeStamp) {
        return timeStamp < file.lastModified();
    }


    public static List<String> readAllLines(final File file) throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        return new ArrayList<>(Arrays.asList(asString.split("\n")));
    }
}