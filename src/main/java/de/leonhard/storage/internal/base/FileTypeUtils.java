package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.enums.FileType;

import java.io.File;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FileTypeUtils {

    public static boolean isType(final File file, final FileType fileType) {
        return getFileType(file).equals(fileType);
    }

    public static String addExtension(String path, FileType fileType) {
        return path + "." + fileType;
    }

    public static String getExtension(String path) {
        return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static FileType getFileType(String type) {
        if (type.equalsIgnoreCase("json")) {
            return FileType.JSON;
        } else if (type.equalsIgnoreCase("yml")) {
            return FileType.YAML;
        } else if (type.equalsIgnoreCase("toml")) {
            return FileType.TOML;
        } else if (type.equalsIgnoreCase("csv")) {
            return FileType.CSV;
        } else if (type.equalsIgnoreCase("ls")) {
            return FileType.LIGHTNING;
        } else {
            return null;
        }
    }

    public static FileType getFileType(File file) {
        return getFileType(getExtension(file));
    }

}