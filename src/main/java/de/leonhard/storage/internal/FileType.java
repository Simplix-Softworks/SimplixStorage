package de.leonhard.storage.internal;

import de.leonhard.storage.util.FileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@Getter
@RequiredArgsConstructor
public enum FileType {
    JSON("json"),
    YAML("yml"),
    TOML("toml");

    private final @NotNull String extension;

    public static @Nullable FileType fromFile(final @NotNull File file) {
        return fromExtension(FileUtils.getExtension(file));
    }

    public static @Nullable FileType fromExtension(final String type) {
        for (val value : values()) {
            if (!value.extension.equalsIgnoreCase(type)) continue;
            return value;
        }

        return null;
    }

    public static @Nullable FileType fromExtension(final @NotNull File file) {
        return fromExtension(FileUtils.getExtension(file));
    }
}
