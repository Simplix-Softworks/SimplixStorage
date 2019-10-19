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


@SuppressWarnings({"unused"})
public class LightningStorage {

	private final File file;
	private final String name;
	private final String path;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;

	private LightningStorage(final File file) {
		this.file = file;
		this.name = null;
		this.path = null;
	}

	private LightningStorage(final String name, final String path) {
		this.file = null;
		this.name = name;
		this.path = path;
	}

	// <Builder initialization>
	public static LightningStorage create(final File file) {
		return new LightningStorage(file);
	}

	public static LightningStorage create(final String name, final String path) {
		return new LightningStorage(name, path);
	}


	public final LightningStorage fromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public final LightningStorage setReloadSetting(final ReloadSettings reloadSetting) {
		this.reloadSettings = reloadSetting;
		return this;
	}
	// </Builder initialization>


	// <Create Datafile>
	public final CSVFile asCSV() throws InvalidFileTypeException {
		return this.file == null
			   ? new CSVFile(new File(path, FileTypeUtils.addExtension(name, FileType.CSV)), inputStream, reloadSettings)
			   : new CSVFile(file, inputStream, reloadSettings);
	}

	public final JsonFile asJson() throws InvalidFileTypeException {
		return this.file == null
			   ? new JsonFile(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), inputStream, reloadSettings)
			   : new JsonFile(file, inputStream, reloadSettings);
	}

	public final JsonConfig asJsonConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new JsonConfig(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), inputStream, reloadSettings)
			   : new JsonConfig(file, inputStream, reloadSettings);
	}

	public final LightningConfig asLightningConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new LightningConfig(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), inputStream, reloadSettings)
			   : new LightningConfig(file, inputStream, reloadSettings);
	}

	public final LightningFile asLightningFile() throws InvalidFileTypeException {
		return this.file == null
			   ? new LightningFile(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), inputStream, reloadSettings)
			   : new LightningFile(file, inputStream, reloadSettings);
	}

	public final TomlFile asToml() throws InvalidFileTypeException {
		return this.file == null
			   ? new TomlFile(new File(path, FileTypeUtils.addExtension(path, FileType.TOML)), inputStream, reloadSettings)
			   : new TomlFile(file, inputStream, reloadSettings);
	}

	public final TomlConfig asTomlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new TomlConfig(new File(path, FileTypeUtils.addExtension(name, FileType.TOML)), inputStream, reloadSettings)
			   : new TomlConfig(file, inputStream, reloadSettings);
	}

	public final YamlFile asYaml() throws InvalidFileTypeException {
		return this.file == null
			   ? new YamlFile(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSettings)
			   : new YamlFile(file, inputStream, reloadSettings);
	}

	public final YamlConfig asYamlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new YamlConfig(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSettings)
			   : new YamlConfig(file, inputStream, reloadSettings);
	}
	// </Create Datafile>

	@Override
	public String toString() {
		return "LightningStorageBuilder: File: " + (this.file == null ? this.name : this.file.getName()) + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream) + (this.reloadSettings == null ? "" : ", ReloadSetting: " + this.reloadSettings);
	}
}