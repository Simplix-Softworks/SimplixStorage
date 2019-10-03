package de.leonhard.storage.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess", "ResultOfMethodCallIgnored"})
public class FileUtils {

    public static File getFile(String filename, String path) {
        return new File(path, filename);
    }

    public static boolean exists(String filename, String path) {
        return getFile(filename, path).exists();
    }

    public static boolean hasNotChanged(final File file, final long timeStamp) {
        return file.lastModified() <= timeStamp;
    }

    public static List<String> readAllLines(final File file) throws IOException {
        return getStrings(file);
    }

    public static List<String> getStrings(File file) throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        return new ArrayList<>(Arrays.asList(asString.split("\n")));
    }

    public static void deleteFile(String filename, String path) {
        final File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
    }
}