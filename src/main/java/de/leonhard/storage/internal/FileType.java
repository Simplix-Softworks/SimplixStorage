package de.leonhard.storage.internal;

import de.leonhard.storage.utils.FileUtils;
import lombok.Getter;

import java.io.File;

public enum FileType {
    JSON("json"),
    YAML("yml"),
    TOML("toml"),
    CSV("csv"),
    LS("ls");

    @Getter
    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public static FileType fromFile(final File file) {
        return fromExtension(FileUtils.getExtension(file));
    }

    public static FileType fromExtension(String type) {
        for (FileType value : values()) {
            if (!value.extension.equalsIgnoreCase(type))
                continue;
            return value;
        }
        return null;
    }

    public static FileType fromExtension(File file) {
        return fromExtension(FileUtils.getExtension(file));
    }

    @Override
    public String toString() {
        return extension;
    }
}