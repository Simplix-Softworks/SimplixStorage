package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.config.TomlConfig;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.datafiles.raw.*;
import de.leonhard.storage.internal.utils.FileTypeUtils;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.Valid;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningStorage {

	private final File file;
	private final File directory;
	private final String name;
	private final String path;
	private InputStream inputStream;
	private FlatFile.ReloadSetting reloadSetting;
	private FlatFile.ConfigSetting configSetting;
	private FileData.Type fileDataType;

	private LightningStorage(@NotNull final File file) {
		this.file = file;
		this.directory = null;
		this.name = null;
		this.path = null;
	}

	private LightningStorage(@Nullable final String path, @NotNull final String name) {
		this.file = null;
		this.directory = null;
		this.name = name;
		this.path = path;
	}

	private LightningStorage(@Nullable final File directory, @NotNull final String name) {
		this.file = null;
		this.directory = directory;
		this.name = name;
		this.path = null;
	}

	// <Builder initialization>
	public static LightningStorage create(@NotNull final File file) {
		Valid.notNull(file, "File must not be null");
		return new LightningStorage(file);
	}

	public static LightningStorage create(@Nullable final String path, @NotNull final String name) {
		Valid.notNull(name, "Name must not be null");
		return new LightningStorage(path, name);
	}

	public static LightningStorage create(@Nullable final File directory, @NotNull final String name) {
		Valid.notNull(name, "Name must not be null");
		return new LightningStorage(directory, name);
	}
	// </Builder initialization>


	// <optional Builder arguments>
	public final LightningStorage fromInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public final LightningStorage fromFile(final File file) {
		this.inputStream = FileUtils.createNewInputStream(file);
		return this;
	}

	public final LightningStorage fromResource(final String resource) {
		LightningStorage.class.getClassLoader().getResourceAsStream(resource);
		return this;
	}

	public final LightningStorage reloadSetting(final FlatFile.ReloadSetting reloadSetting) {
		this.reloadSetting = reloadSetting;
		return this;
	}

	public final LightningStorage configSetting(final FlatFile.ConfigSetting configSetting) {
		this.configSetting = configSetting;
		return this;
	}

	public final LightningStorage fileDataType(final FileData.Type fileDataType) {
		this.fileDataType = fileDataType;
		return this;
	}
	// </optional Builder arguments>


	// <Create Datafile>
	public final CSVFile asCSV() {
		return this.file == null
			   ? (this.directory == null
				  ? new CSVFile(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.CSV)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new CSVFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.CSV)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new CSVFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final JsonFile asJson() {
		return this.file == null
			   ? (this.directory == null
				  ? new JsonFile(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new JsonFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new JsonFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final LightningConfig asLightningConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new LightningConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new LightningConfig(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final LightningFile asLightningFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningFile(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new LightningFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new LightningFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final TomlFile asToml() {
		return this.file == null
			   ? (this.directory == null
				  ? new TomlFile(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new TomlFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new TomlFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final TomlConfig asTomlConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new TomlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new TomlConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new TomlConfig(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final YamlFile asYaml() {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlFile(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new YamlFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new YamlFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	public final YamlConfig asYamlConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new YamlConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new YamlConfig(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}
	// </Create Datafile>

	@Override
	public String toString() {
		return "LightningStorageBuilder: File: "
			   + (this.file == null ? this.name : this.file.getName())
			   + ", Directory: " + (this.directory == null ? this.path : this.directory.getAbsolutePath())
			   + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream)
			   + (this.reloadSetting == null ? "" : ", ReloadSetting: " + this.reloadSetting)
			   + (this.configSetting == null ? "" : ", ConfigSetting: " + this.configSetting)
			   + (this.fileDataType == null ? "" : ", DataType: " + this.fileDataType);
	}
}