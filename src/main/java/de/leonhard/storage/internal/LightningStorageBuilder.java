package de.leonhard.storage.internal;

import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.config.JsonConfig;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.config.TomlConfig;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.datafiles.raw.*;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;

import java.io.File;
import java.io.InputStream;

@SuppressWarnings("unused")
public class LightningStorageBuilder {

    private File file;
    private InputStream inputStream;
    private ReloadSettings reloadSettings;


    public LightningStorageBuilder(final File file) {
        this.file = file;
    }

    public LightningStorageBuilder(final String name, final String path) {
        this.file = new File(path, FileTypeUtils.addExtension(name, FileType.YAML));
    }

    public LightningStorageBuilder fromInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public LightningStorageBuilder setReloadSetting(final ReloadSettings reloadSetting) {
        this.reloadSettings = reloadSetting;
        return this;
    }

    public LightningFile createLightningFile() throws InvalidFileTypeException {
        return new LightningFile(file, inputStream, reloadSettings);
    }

    public LightningConfig createLightningConfig() throws InvalidFileTypeException {
        return new LightningConfig(file, inputStream, reloadSettings);
    }

    public CSVFile createCSV() throws InvalidFileTypeException {
        return new CSVFile(file, inputStream, reloadSettings);
    }

    public JsonFile createJson() throws InvalidFileTypeException {
        return new JsonFile(file, inputStream, reloadSettings);
    }

    public JsonConfig createJsonConfig() throws InvalidFileTypeException {
        return new JsonConfig(file, inputStream, reloadSettings);
    }

    public TomlFile createToml() throws InvalidFileTypeException {
        return new TomlFile(file, inputStream, reloadSettings);
    }

    public TomlConfig createTomlConfig() throws InvalidFileTypeException {
        return new TomlConfig(file, inputStream, reloadSettings);
    }

    public YamlFile createYaml() throws InvalidFileTypeException {
        return new YamlFile(file, inputStream, reloadSettings);
    }

    public YamlConfig createYamlConfig() throws InvalidFileTypeException {
        return new YamlConfig(file, inputStream, reloadSettings);
    }
}