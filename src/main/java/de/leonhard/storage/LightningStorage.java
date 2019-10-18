package de.leonhard.storage;

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
public class LightningStorage {

	final private File file;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;

	private LightningStorage(final File file) {
		this.file = file;
	}

	public static LightningStorage create(final File file) {
		return new LightningStorage(file);
	}

	public static LightningStorage create(final String name, final String path) {
		return new LightningStorage(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)));
	}

	public final CSVFile asCSV() throws InvalidFileTypeException {
		return new CSVFile(file, inputStream, reloadSettings);
	}

	public final JsonFile asJson() throws InvalidFileTypeException {
		return new JsonFile(file, inputStream, reloadSettings);
	}

	public final JsonConfig asJsonConfig() throws InvalidFileTypeException {
		return new JsonConfig(file, inputStream, reloadSettings);
	}

	public final LightningConfig asLightningConfig() throws InvalidFileTypeException {
		return new LightningConfig(file, inputStream, reloadSettings);
	}

	public final LightningFile asLightningFile() throws InvalidFileTypeException {
		return new LightningFile(file, inputStream, reloadSettings);
	}

	public final TomlFile asToml() throws InvalidFileTypeException {
		return new TomlFile(file, inputStream, reloadSettings);
	}

	public final TomlConfig asTomlConfig() throws InvalidFileTypeException {
		return new TomlConfig(file, inputStream, reloadSettings);
	}

	public final YamlFile asYaml() throws InvalidFileTypeException {
		return new YamlFile(file, inputStream, reloadSettings);
	}

	public final YamlConfig asYamlConfig() throws InvalidFileTypeException {
		return new YamlConfig(file, inputStream, reloadSettings);
	}

	public final LightningStorage fromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public final LightningStorage setReloadSetting(final ReloadSettings reloadSetting) {
		this.reloadSettings = reloadSetting;
		return this;
	}

	@Override
	public String toString() {
		return "LightningStorageBuilder: File:" + this.file.getName() + ", InputStream: " + this.inputStream + ", ReloadSetting: " + this.reloadSettings;
	}
}