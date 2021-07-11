package de.leonhard.storage.internal;

import de.leonhard.storage.util.FileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.File;

@Getter
@RequiredArgsConstructor
public enum FileType {
    JSON("json"),
    YAML("yml"),
    TOML("toml");

    private final String extension;

    public static FileType fromFile(final File file) {
        return fromExtension(FileUtils.getExtension(file));
    }

    public static FileType fromExtension(final String type)
    {
        for (val value : values()) {
            if (!value.extension.equalsIgnoreCase(type)) continue;
            return value;
        }
        return null;
    }

    public static FileType fromExtension(final File file) {
        return fromExtension(FileUtils.getExtension(file));
    }
}
