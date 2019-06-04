package de.leonhard.storage.util;

import java.io.File;

public class FileUtils {


    public static boolean hasChanged(final File file, final long timeStamp) {
        return file.lastModified() > timeStamp;
    }
}
