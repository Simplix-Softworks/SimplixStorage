package de.leonhard.storage;

import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.*;
import de.leonhard.storage.internal.enums.ReloadSettings;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningStorage extends FileBuilder {

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

	private LightningStorage(final String path, final String name) {
		this.file = null;
		this.name = name;
		this.path = path;
	}

	// <Builder initialization>
	public static LightningStorage create(final File file) {
		return new LightningStorage(file);
	}

	public static LightningStorage create(final String path, final String name) {
		return new LightningStorage(path, name);
	}


	public final LightningStorage fromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public final LightningStorage withCustomReloadSetting(final ReloadSettings reloadSetting) {
		this.reloadSettings = reloadSetting;
		return this;
	}
	// </Builder initialization>


	// <Create Datafile>
	public final CSVFile asCSV() throws InvalidFileTypeException {
		return asCSV(file, path, name, inputStream, reloadSettings);
	}

	public final JsonFile asJson() throws InvalidFileTypeException {
		return asJson(file, path, name, inputStream, reloadSettings);
	}

	public final JsonConfig asJsonConfig() throws InvalidFileTypeException {
		return asJsonConfig(file, path, name, inputStream, reloadSettings);
	}

	public final LightningConfig asLightningConfig() throws InvalidFileTypeException {
		return asLightningConfig(file, path, name, inputStream, reloadSettings);
	}

	public final de.leonhard.storage.internal.datafiles.LightningFile asLightningFile() throws InvalidFileTypeException {
		return asLightningFile(file, path, name, inputStream, reloadSettings);
	}

	public final TomlFile asToml() throws InvalidFileTypeException {
		return asToml(file, path, name, inputStream, reloadSettings);
	}

	public final TomlConfig asTomlConfig() throws InvalidFileTypeException {
		return asTomlConfig(file, path, name, inputStream, reloadSettings);
	}

	public final YamlFile asYaml() throws InvalidFileTypeException {
		return asYaml(file, path, name, inputStream, reloadSettings);
	}

	public final YamlConfig asYamlConfig() throws InvalidFileTypeException {
		return asYamlConfig(file, path, name, inputStream, reloadSettings);
	}
	// </Create Datafile>

	@Override
	public String toString() {
		return "LightningStorageBuilder: File: " + (this.file == null ? this.name : this.file.getName()) + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream) + (this.reloadSettings == null ? "" : ", ReloadSetting: " + this.reloadSettings);
	}
}