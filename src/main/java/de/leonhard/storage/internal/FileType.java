package de.leonhard.storage.internal;

import de.leonhard.storage.util.FileUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public enum FileType {
	JSON("json"),
	YAML("yml"),
	TOML("toml"),
	CSV("csv"),
	LS("ls");

	private final String extension;

	public static FileType fromFile(File file) {
		return fromExtension(FileUtils.getExtension(file));
	}

	public static FileType fromExtension(String type) {
		for (FileType value : values()) {
			if (!value.extension.equalsIgnoreCase(type)) {
				continue;
			}
			return value;
		}
		return null;
	}

	public static FileType fromExtension(File file) {
		return fromExtension(FileUtils.getExtension(file));
	}
}