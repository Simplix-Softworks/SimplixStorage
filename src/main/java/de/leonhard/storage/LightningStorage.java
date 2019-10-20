package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.config.JsonConfig;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.config.TomlConfig;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.datafiles.raw.*;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.Valid;
import java.io.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningStorage {

	private final File file;
	private final File directory;
	private final String name;
	private final String path;
	private InputStream inputStream;
	private ReloadSetting reloadSetting;

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

	public final LightningStorage fromFile(final File file) throws FileNotFoundException {
		this.inputStream = new BufferedInputStream(new FileInputStream(file));
		return this;
	}

	public final LightningStorage fromResource(final String resource) {
		LightningStorage.class.getClassLoader().getResourceAsStream(resource);
		return this;
	}

	public final LightningStorage reloadSetting(final ReloadSetting reloadSetting) {
		this.reloadSetting = reloadSetting;
		return this;
	}
	// </optional Builder arguments>


	// <Create Datafile>
	public final CSVFile asCSV() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new CSVFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new CSVFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new CSVFile(this.file, this.inputStream, this.reloadSetting);
	}

	public final JsonFile asJson() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new JsonFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new JsonFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new JsonFile(this.file, this.inputStream, this.reloadSetting);
	}

	public final JsonConfig asJsonConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new JsonConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new JsonConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new JsonConfig(this.file, this.inputStream, this.reloadSetting);
	}

	public final LightningConfig asLightningConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new LightningConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new LightningConfig(this.file, this.inputStream, this.reloadSetting);
	}

	public final LightningFile asLightningFile() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new LightningFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new LightningFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new LightningFile(this.file, this.inputStream, this.reloadSetting);
	}

	public final TomlFile asToml() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new TomlFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new TomlFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new TomlFile(this.file, this.inputStream, this.reloadSetting);
	}

	public final TomlConfig asTomlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new TomlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new TomlConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new TomlConfig(this.file, this.inputStream, this.reloadSetting);
	}

	public final YamlFile asYaml() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlFile(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new YamlFile(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new YamlFile(this.file, this.inputStream, this.reloadSetting);
	}

	public final YamlConfig asYamlConfig() throws InvalidFileTypeException {
		return this.file == null
			   ? (this.directory == null
				  ? new YamlConfig(new File(this.path, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting)
				  : new YamlConfig(new File(this.directory, FileTypeUtils.addExtension(this.name, FileType.CSV)), this.inputStream, this.reloadSetting))
			   : new YamlConfig(this.file, this.inputStream, this.reloadSetting);
	}
	// </Create Datafile>

	@Override
	public String toString() {
		return "LightningStorageBuilder: File: "
			   + (this.file == null ? this.name : this.file.getName())
			   + ", Directory: " + (this.directory == null ? this.path : this.directory.getAbsolutePath())
			   + (this.inputStream == null ? "" : ", InputStream: " + this.inputStream)
			   + (this.reloadSetting == null ? "" : ", ReloadSetting: " + this.reloadSetting);
	}
}