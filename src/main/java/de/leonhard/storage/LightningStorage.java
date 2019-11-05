package de.leonhard.storage;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.FileTypeUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Builder Class
 */
@SuppressWarnings("unused")
public class LightningStorage {

	private final File file;
	private final File directory;
	private final String name;
	private final String path;
	private BufferedInputStream inputStream;
	private Reload reloadSetting;
	private boolean preserveComments = true;
	private DataType dataType;

	// <local Constructors>
	private LightningStorage(final @NotNull File file) {
		this.file = file;
		this.directory = null;
		this.name = null;
		this.path = null;
	}

	private LightningStorage(final @NotNull String directory, final @NotNull String name) {
		this.file = null;
		this.directory = null;
		this.name = name;
		this.path = directory;
	}

	private LightningStorage(final @NotNull File directory, final @NotNull String name) {
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
	public static LightningStorage create(final @NotNull File file) {
		return new LightningStorage(Objects.notNull(file, "File must not be null"));
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param file the Path value of the File to be used.
	 */
	public static LightningStorage create(final @NotNull Path file) {
		return new LightningStorage(Objects.notNull(file, "File must not be null").toFile());
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param name the name of the File to be used to be used.
	 */
	public static LightningStorage create(final @NotNull String name) {
		return new LightningStorage(new File(Objects.notNull(name, "Name must not be null")));
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param directory the directory to the File to be used.
	 * @param name      the name of the File to be used.
	 */
	public static LightningStorage create(final @NotNull String directory, final @NotNull String name) {
		return new LightningStorage(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null"));
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param directory the directory of the File to be used.
	 * @param name      the name of the File to be used.
	 */
	public static LightningStorage create(final @NotNull File directory, final @NotNull String name) {
		return new LightningStorage(Objects.notNull(directory, "Directory must not be null"), Objects.notNull(name, "Name must not be null"));
	}

	/**
	 * Initiate a File through LightningStorage.
	 *
	 * @param directory the Path value of the directory of the File to be used.
	 * @param name      the name of the File to be used.
	 */
	public static LightningStorage create(final @NotNull Path directory, final @NotNull String name) {
		return new LightningStorage(Objects.notNull(directory, "Directory must not be null").toFile(), Objects.notNull(name, "Name must not be null"));
	}
	// </Builder initialization>


	// <optional Builder arguments>

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param inputStream the Data to be imported.
	 */
	public final LightningStorage fromInputStream(final @Nullable BufferedInputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param file the File to be imported from.
	 */
	public final LightningStorage fromFile(final @Nullable File file) {
		this.inputStream = file == null ? null : FileUtils.createNewInputStream(file);
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param file the File to be imported from.
	 */
	public final LightningStorage fromFile(final @Nullable Path file) {
		this.inputStream = file == null ? null : FileUtils.createNewInputStream(file.toFile());
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported from.
	 * @param name      the name of the File to be imported from.
	 */
	public final LightningStorage fromFile(final @Nullable String directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = FileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported from.
	 * @param name      the name of the File to be imported from.
	 */
	public final LightningStorage fromFile(final @Nullable File directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = FileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory, name));
		}
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param directory the directory of the File to be imported from.
	 * @param name      the name of the File to be imported from.
	 */
	public final LightningStorage fromFile(final @Nullable Path directory, final @Nullable String name) {
		if (name != null) {
			this.inputStream = FileUtils.createNewInputStream(directory == null ? new File(name) : new File(directory.toFile(), name));
		}
		return this;
	}

	/**
	 * Import the given Data to the File if said does not exist.
	 *
	 * @param resource the internal resource to be imported from.
	 */
	public final LightningStorage fromResource(final @Nullable String resource) {
		this.inputStream = resource == null ? null : FileUtils.createNewInputStream(resource);
		return this;
	}

	/**
	 * Set the ReloadSetting for the File.
	 *
	 * @param reloadSetting the ReloadSetting to be set(default is INTELLIGENT)
	 */
	public final LightningStorage reloadSetting(final @Nullable Reload reloadSetting) {
		this.reloadSetting = reloadSetting;
		return this;
	}

	/**
	 * Set the CommentSetting for the File.
	 *
	 * @param preserveComments the CommentSetting to be set(Default for Configs is true, otherwise it's false)
	 */
	public final LightningStorage commentSetting(final boolean preserveComments) {
		this.preserveComments = preserveComments;
		return this;
	}

	/**
	 * Set the way the Data is stored.
	 *
	 * @param dataType the DataType to be set(Default is AUTOMATIC, which depends on the FileType and the ReloadSetting)
	 */
	public final LightningStorage dataType(final @Nullable DataType dataType) {
		this.dataType = dataType;
		return this;
	}
	// </optional Builder arguments>


	// <Create Datafile>s

	/**
	 * Create a Json-Type File.
	 */
	public final JsonFile asJsonFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalJsonFile(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.dataType)
				  : new LocalJsonFile(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.JSON)), this.inputStream, this.reloadSetting, this.dataType))
			   : new LocalJsonFile(this.file, this.inputStream, this.reloadSetting, this.dataType);
	}

	/**
	 * Create a LightningConfig-Type File.
	 */
	public final LightningConfig asLightningConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalLightningConfig(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType)
				  : new LocalLightningConfig(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType))
			   : new LocalLightningConfig(this.file, this.inputStream, this.reloadSetting, this.preserveComments, this.dataType);
	}

	/**
	 * Create a Lightning-Type File.
	 */
	public final LightningFile asLightningFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalLightningFile(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType)
				  : new LocalLightningFile(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.LIGHTNING)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType))
			   : new LocalLightningFile(this.file, this.inputStream, this.reloadSetting, this.preserveComments, this.dataType);
	}

	/**
	 * Create a Toml-Type File.
	 */
	public final TomlFile asTomlFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalTomlFile(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.dataType)
				  : new LocalTomlFile(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.TOML)), this.inputStream, this.reloadSetting, this.dataType))
			   : new LocalTomlFile(this.file, this.inputStream, this.reloadSetting, this.dataType);
	}

	/**
	 * Create a Yaml-Type File.
	 */
	public final YamlFile asYamlFile() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalYamlFile(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType)
				  : new LocalYamlFile(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType))
			   : new LocalYamlFile(this.file, this.inputStream, this.reloadSetting, this.preserveComments, this.dataType);
	}

	/**
	 * Create a YamlConfig-Type File.
	 */
	public final YamlConfig asYamlConfig() {
		return this.file == null
			   ? (this.directory == null
				  ? new LocalYamlConfig(new File(this.path, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType)
				  : new LocalYamlConfig(new File(this.directory, FileTypeUtils.addExtension(Objects.notNull(this.name), FlatFile.FileType.YAML)), this.inputStream, this.reloadSetting, this.preserveComments, this.dataType))
			   : new LocalYamlConfig(this.file, this.inputStream, this.reloadSetting, this.preserveComments, this.dataType);
	}
	// </Create Datafile>


	@Override
	public String toString() {
		return "LightningStorageBuilder: File: "
			   + (this.file == null ? this.name : this.file.getName())
			   + ", Directory: " + (this.directory == null ? this.path : this.directory.getAbsolutePath())
			   + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream)
			   + (this.reloadSetting == null ? "" : ", ReloadSetting: " + this.reloadSetting)
			   + (this.dataType == null ? "" : ", DataType: " + this.dataType)
			   + ", CommentSetting: " + this.preserveComments;
	}


	private static class LocalJsonFile extends de.leonhard.storage.internal.datafiles.raw.JsonFile {

		private LocalJsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, dataType);
		}
	}

	private static class LocalLightningConfig extends de.leonhard.storage.internal.datafiles.config.LightningConfig {

		private LocalLightningConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final boolean preserveComments, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, preserveComments, dataType);
		}
	}

	private static class LocalLightningFile extends de.leonhard.storage.internal.datafiles.raw.LightningFile {

		private LocalLightningFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final boolean preserveComments, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, preserveComments, dataType);
		}
	}

	private static class LocalTomlFile extends de.leonhard.storage.internal.datafiles.raw.TomlFile {

		private LocalTomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, dataType);
		}
	}

	private static class LocalYamlFile extends de.leonhard.storage.internal.datafiles.raw.YamlFile {

		private LocalYamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final boolean preserveComments, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, preserveComments, dataType);
		}
	}

	private static class LocalYamlConfig extends de.leonhard.storage.internal.datafiles.config.YamlConfig {

		private LocalYamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final boolean preserveComments, final @Nullable DataType dataType) {
			super(file, inputStream, reloadSetting, preserveComments, dataType);
		}
	}
}