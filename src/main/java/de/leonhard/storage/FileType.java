package de.leonhard.storage;

public enum FileType {
    JSON(".json"),
    YAML(".yml"),
    TOML(".toml"),
    CSV(".csv"),
    LS(".ls");


    private String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
