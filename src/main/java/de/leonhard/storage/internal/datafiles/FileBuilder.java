package de.leonhard.storage.internal.datafiles;

import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public abstract class FileBuilder {

	protected final CSVFile asCSV(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new CSVFile(new File(path, FileTypeUtils.addExtension(name, FileType.CSV)), inputStream, reloadSettings)
			   : new CSVFile(file, inputStream, reloadSettings);
	}

	protected final JsonFile asJson(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new JsonFile(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), inputStream, reloadSettings)
			   : new JsonFile(file, inputStream, reloadSettings);
	}

	protected final JsonConfig asJsonConfig(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new JsonConfig(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), inputStream, reloadSettings)
			   : new JsonConfig(file, inputStream, reloadSettings);
	}

	protected final LightningConfig asLightningConfig(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new LightningConfig(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), inputStream, reloadSettings)
			   : new LightningConfig(file, inputStream, reloadSettings);
	}

	protected final LightningFile asLightningFile(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new LightningFile(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), inputStream, reloadSettings)
			   : new LightningFile(file, inputStream, reloadSettings);
	}

	protected final TomlFile asToml(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new TomlFile(new File(path, FileTypeUtils.addExtension(path, FileType.TOML)), inputStream, reloadSettings)
			   : new TomlFile(file, inputStream, reloadSettings);
	}

	protected final TomlConfig asTomlConfig(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new TomlConfig(new File(path, FileTypeUtils.addExtension(name, FileType.TOML)), inputStream, reloadSettings)
			   : new TomlConfig(file, inputStream, reloadSettings);
	}

	protected final YamlFile asYaml(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new YamlFile(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSettings)
			   : new YamlFile(file, inputStream, reloadSettings);
	}

	protected final YamlConfig asYamlConfig(final File file, final String path, final String name, final InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		return file == null
			   ? new YamlConfig(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSettings)
			   : new YamlConfig(file, inputStream, reloadSettings);
	}
}