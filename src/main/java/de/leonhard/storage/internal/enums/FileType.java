package de.leonhard.storage.internal.enums;


public enum FileType {

	JSON("json"),
	YAML("yml"),
	TOML("toml"),
	CSV("csv"),
	LIGHTNING("ls");


	private String extension;

	FileType(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return extension;
	}
}