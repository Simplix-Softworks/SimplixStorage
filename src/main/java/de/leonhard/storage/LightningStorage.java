package de.leonhard.storage;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.lightningstorage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.lightningstorage.internal.datafiles.raw.*;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import de.leonhard.storage.lightningstorage.utils.basic.FileTypeUtils;
import de.leonhard.storage.lightningstorage.utils.basic.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused"})
public class LightningStorage {

	private final File file;
	private final File directory;
	private final String name;
	private final String path;
	private InputStream inputStream;
	private FlatFile.ReloadSetting reloadSetting;
	private FlatFile.ConfigSetting configSetting;
	private FileData.Type fileDataType;

	// <local Constructors>
	private LightningStorage(@NotNull final File file) {
		this.file = file;
		this.directory = null;
		this.name = null;
		this.path = null;
	}

	private LightningStorage(@NotNull final String path, @NotNull final String name) {
		this.file = null;
		this.directory = null;
		this.name = name;
		this.path = path;
	}

	private LightningStorage(@NotNull final File directory, @NotNull final String name) {
		this.file = null;
		this.directory = directory;
		this.name = name;
		this.path = null;
	}
	// </local Constructors>


	// <Builder initialization>

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param file the File to be used.
	 */
	public static LightningStorage create(@NotNull final File file) {
		Valid.notNull(file, "File must not be null");
		return new LightningStorage(file);
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param path the path to the File to be used.
	 * @param name the name of the File to be used.
	 */
	public static LightningStorage create(@NotNull final String path, @NotNull final String name) {
		Valid.notNull(name, "Name must not be null");
		Valid.notNull(path, "Path must not be null");
		return new LightningStorage(path, name);
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param directory the directory of the File to be used.
	 * @param name      the name of the File to be used.
	 */
	public static LightningStorage create(@NotNull final File directory, @NotNull final String name) {
		Valid.notNull(name, "Name must not be null");
		Valid.notNull(directory, "Directory must not be null");
		return new LightningStorage(directory, name);
	}
	// </Builder initialization>


	// <optional Builder arguments>

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param inputStream the Data to be imported.
	 */
	public final LightningStorage fromInputStream(@Nullable final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param file the File to be imported from.
	 */
	public final LightningStorage fromFile(@Nullable final File file) {
		this.inputStream = file == null ? null : FileUtils.createNewInputStream(file);
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param resource the internal resource to be imported from.
	 */
	public final LightningStorage fromResource(@Nullable final String resource) {
		this.inputStream = resource == null ? null : FileUtils.createNewInputStream(resource);
		return this;
	}

	/**
	 * Set the ReloadSetting for the File.
	 *
	 * @param reloadSetting the ReloadSetting to be set(default is INTELLIGENT)
	 */
	public final LightningStorage reloadSetting(@Nullable final FlatFile.ReloadSetting reloadSetting) {
		this.reloadSetting = reloadSetting;
		return this;
	}

	/**
	 * Set the ConfigSetting for the File.
	 *
	 * @param configSetting the ConfigSetting to be set(Default for Configs is PRESERVE_COMMENTS, otherwise it's SKIP_COMMENTS)
	 */
	public final LightningStorage configSetting(@Nullable FlatFile.ConfigSetting configSetting) {
		this.configSetting = configSetting;
		return this;
	}

	/**
	 * Set the way the Data is stored.
	 *
	 * @param fileDataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
	 */
	public final LightningStorage fileDataType(@Nullable final FileData.Type fileDataType) {
		this.fileDataType = fileDataType;
		return this;
	}
	// </optional Builder arguments>


	// <Create Datafile>

	/**
	 * Create a CSV-Type File.
	 */
	public final CSVFile asCSV() {
		return this.file == null
			   ? (this.directory == null
				  ? new CSVFile(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.CSV)), this.inputStream, this.reloadSetting, this.fileDataType)
				  : new CSVFile(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.CSV)), this.inputStream, this.reloadSetting, this.fileDataType))
			   : new CSVFile(this.file, this.inputStream, this.reloadSetting, this.fileDataType);
	}

	/**
	 * Create a Json-Type File.
	 */
	public final JsonFile asJson() {
		return this.file == null
			   ? (this.directory == null
				  ? new JsonFile(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.fileDataType)
				  : new JsonFile(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.fileDataType))
			   : new JsonFile(this.file, this.inputStream, this.reloadSetting, this.fileDataType);
	}

	/**
	 * Create a LightningConfig-Type File.
	 */
	public final LightningConfig asLightningConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningConfig(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new LightningConfig(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new LightningConfig(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	/**
	 * Create a Lightning-Type File.
	 */
	public final LightningFile asLightningFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningFile(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new LightningFile(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new LightningFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	/**
	 * Create a Toml-Type File.
	 */
	public final TomlFile asToml() {
		return this.file == null
			   ? (this.directory == null
				  ? new TomlFile(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.fileDataType)
				  : new TomlFile(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.fileDataType))
			   : new TomlFile(this.file, this.inputStream, this.reloadSetting, this.fileDataType);
	}

	/**
	 * Create a Yaml-Type File.
	 */
	public final YamlFile asYaml() {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlFile(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new YamlFile(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
			   : new YamlFile(this.file, this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType);
	}

	/**
	 * Create a YamlConfig-Type File.
	 */
	public final YamlConfig asYamlConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlConfig(new File(this.path, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType)
				  : new YamlConfig(new File(this.directory, FileTypeUtils.addExtension(Objects.requireNonNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.configSetting, this.fileDataType))
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