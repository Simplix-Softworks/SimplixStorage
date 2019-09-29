package de.leonhard.storage.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    private static File getFile(String filename, String path) {
        return new File(path, filename);
    }

    public static boolean exists(String filename, String path) {
        return getFile(filename, path).exists();
    }

    public final static File getOrCreate(final File file) {
        if (file.exists())
            return file;
        final String path = file.getAbsolutePath();
        final int lastIndex = path.lastIndexOf('/');
        final File directory = new File(path.substring(0, Math.max(0, path.lastIndexOf("/"))));

        directory.mkdirs(); //Creating folder

        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.err.println("Exception while creating File '" + file.getName() + "'");
            System.err.println("Path: '" + file.getAbsolutePath() + "'");
            ex.printStackTrace();
        }
        return file;
    }


    public static File[] getFiles(final String directory, String extension) {
        if (extension.startsWith("."))
            extension = extension.substring(1);

        final File dataFolder = new File(directory);

        if (!dataFolder.exists())
            dataFolder.mkdirs();

        final String finalExtension = extension;

        return dataFolder.listFiles(file -> !file.isDirectory() && file.getName().endsWith("." + finalExtension));
    }

    public static boolean hasNotChanged(final File file, final long timeStamp) {
        return file.lastModified() <= timeStamp;
    }

    public static List<String> readAllLines(final File file) throws IOException {
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

