package de.leonhard.storage.internal.base.enums;

public enum FileType {

	JSON("json"),
	YAML("yml"),
	TOML("toml"),
	CSV("csv"),
	LIGHTNING("ls"),
	DEFAULT("");


	private String extension;

	FileType(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return extension;
	}
}