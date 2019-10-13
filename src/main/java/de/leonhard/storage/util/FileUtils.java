package de.leonhard.storage.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused"})
public class FileUtils {

    public static boolean hasNotChanged(final File file, final long timeStamp) {
        return file.lastModified() <= timeStamp;
    }

    /*public static File getOrCreate(final File file) {
        if (file.exists()) {
            return file;
        } else {
            try {
                if (file.getAbsoluteFile().getParentFile().exists() || file.getAbsoluteFile().getParentFile().mkdirs()) {
                    if (!file.createNewFile()) {
                        throw new IOException();
                    }
                }
            } catch (IOException ex) {
                System.err.println("Exception while creating File '" + file.getName() + "'");
                System.err.println("Path: '" + file.getAbsolutePath() + "'");
                ex.printStackTrace();
            }
            return file;
        }
    }*/


    public static List<String> readAllLines(final File file) throws IOException {
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        final String asString = new String(fileBytes);
        return new ArrayList<>(Arrays.asList(asString.split("\n")));
    }
}