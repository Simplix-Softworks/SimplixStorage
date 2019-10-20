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
		if (type.equalsIgnoreCase("json")) {
			return JSON;
		} else if (type.equalsIgnoreCase("yml")) {
			return YAML;
		} else if (type.equalsIgnoreCase("toml")) {
			return TOML;
		} else if (type.equalsIgnoreCase("csv")) {
			return CSV;
		} else if (type.equalsIgnoreCase("ls")) {
			return LS;
		} else {
			return null;
		}
	}

	public static FileType fromExtension(File file) {
		return fromExtension(FileUtils.getExtension(file));
	}

	@Override
	public String toString() {
		return extension;
	}
}