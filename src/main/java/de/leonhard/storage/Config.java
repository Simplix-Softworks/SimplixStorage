package de.leonhard.storage;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class Config extends Yaml {

    private String[] header;
    private ConfigSettings configSettings;

    public Config(String name, String path) {
        super(name, path);
    }

    public Config(String name, String path, ReloadSettings reloadSettings) {
        super(name, path, reloadSettings);
    }

    Config(File file) {
        super(file);
    }

    @Override
    public void set(final String key, final Object value) {

    }
}
