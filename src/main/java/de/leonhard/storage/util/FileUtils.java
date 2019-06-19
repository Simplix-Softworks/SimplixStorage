package de.leonhard.storage.util;

import java.io.File;

public class FileUtils {

    public static File getFile(String filename, String path) {
        return new File("plugins/" + path, filename);
    }

    public static boolean exists(String filename, String path) {
        return getFile(filename, path).exists();
    }

    public static boolean hasNotChanged(final File file, final long timeStamp) {
        return file.lastModified() <= timeStamp;
    }

    public static void deleteFile(String filename, String path) {
        final File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
