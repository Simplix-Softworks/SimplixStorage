package de.leonhard.storage;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.config.JsonConfig;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.config.TomlConfig;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.datafiles.raw.*;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.Valid;
import java.io.*;


@SuppressWarnings({"unused"})
public class LightningStorage {

	private final File file;
	private final String name;
	private final String path;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;

	private LightningStorage(@NotNull final File file) {
		this.file = file;
		this.name = null;
		this.path = null;
	}

	private LightningStorage(@Nullable final String path, @NotNull final String name) {
		this.file = null;
		this.name = name;
		this.path = path;
	}

	// <Builder initialization>
	public static LightningStorage create(@NotNull final File file) {
		Valid.notNull(file, "File must not be null");
		return new LightningStorage(file);
	}

	public static LightningStorage create(@NotNull final String path, @NotNull final String name) {
		Valid.notNull(name, "Name must not be null");
		return new LightningStorage(path, name);
	}


	public final LightningStorage fromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public final LightningStorage fromFile(final File file) throws FileNotFoundException {
		this.inputStream = new BufferedInputStream(new FileInputStream(file));
		return this;
	}

	public final LightningStorage fromResource(final String resource) {
		LightningStorage.class.getClassLoader().getResourceAsStream(resource);
		return this;
	}

	public final LightningStorage withCustomReloadSetting(final ReloadSettings reloadSetting) {
		this.reloadSettings = reloadSetting;
		return this;
	}
	// </Builder initialization>


	// <Create Datafile>
	public final CSVFile asCSV() throws InvalidFileTypeException {
		return this.file == null
			   ? new CSVFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSettings)
			   : new CSVFile(this.file, this.inputStream, this.reloadSettings);
	}

	public final JsonFile asJson() throws InvalidFileTypeException {
		return this.file == null
			   ? new JsonFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.JSON)), this.inputStream, this.reloadSettings)
			   : new JsonFile(this.file, this.inputStream, this.reloadSettings);
	}

	public final JsonConfig asJsonConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new JsonConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.JSON)), this.inputStream, this.reloadSettings)
			   : new JsonConfig(this.file, this.inputStream, this.reloadSettings);
	}

	public final LightningConfig asLightningConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new LightningConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.LIGHTNING)), this.inputStream, this.reloadSettings)
			   : new LightningConfig(this.file, this.inputStream, this.reloadSettings);
	}

	public final LightningFile asLightningFile() throws InvalidFileTypeException {
		return this.file == null
			   ? new LightningFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.LIGHTNING)), this.inputStream, this.reloadSettings)
			   : new LightningFile(this.file, this.inputStream, this.reloadSettings);
	}

	public final TomlFile asToml() throws InvalidFileTypeException {
		return this.file == null
			   ? new TomlFile(new File(this.path, FileTypeUtils.addExtension(this.path, FileType.TOML)), this.inputStream, this.reloadSettings)
			   : new TomlFile(this.file, this.inputStream, this.reloadSettings);
	}

	public final TomlConfig asTomlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new TomlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.TOML)), this.inputStream, this.reloadSettings)
			   : new TomlConfig(this.file, this.inputStream, this.reloadSettings);
	}

	public final YamlFile asYaml() throws InvalidFileTypeException {
		return this.file == null
			   ? new YamlFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.YAML)), this.inputStream, this.reloadSettings)
			   : new YamlFile(this.file, this.inputStream, this.reloadSettings);
	}

	public final YamlConfig asYamlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? new YamlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.YAML)), this.inputStream, this.reloadSettings)
			   : new YamlConfig(this.file, this.inputStream, this.reloadSettings);
	}
	// </Create Datafile>

	@Override
	public String toString() {
		return "LightningStorageBuilder: File: " + (this.file == null ? this.name : this.file.getName()) + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream) + (this.reloadSettings == null ? "" : ", ReloadSetting: " + this.reloadSettings);
	}
}