package de.leonhard.storage.util;

import java.io.File;

public class FileUtils {


    public static boolean hasChanged(final File file, final long timeStamp) {
        if (file.lastModified() == timeStamp)
            return false;
        return true;
    }
}
