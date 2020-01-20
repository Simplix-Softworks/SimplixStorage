package de.leonhard.storage;

import de.leonhard.storage.internal.provider.InputStreamProvider;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public final class LightningBuilder {
	private final InputStreamProvider inputStreamProvider;

	private final String path;
	private String name;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;
	private ConfigSettings configSettings;
	private DataType dataType;

	private LightningBuilder(final String name, final String path, final InputStreamProvider inputStreamProvider) {
		this.name = name;
		this.path = path;
		this.inputStreamProvider = inputStreamProvider;
	}

	// ----------------------------------------------------------------------------------------------------
	// Creating our Builder
	// ----------------------------------------------------------------------------------------------------

	public static LightningBuilder fromPath(final String name, final String path) {
		Valid.notNull(name, "Name mustn't be null");
		return new LightningBuilder(name, path, LightningProviders.inputStreamProvider());
	}

	public static LightningBuilder fromPath(final Path path) {
		return fromFile(path.toFile());
	}

	public static LightningBuilder fromFile(final File file) {
		Valid.notNull(file, "File mustn't be null");
		//File shouldn't be a directory
		Valid.checkBoolean(!file.isDirectory(),
				"File mustn't be a directory.",
				"Please use from Directory to use a directory",
				"This is due to Java-Internals");

		return new LightningBuilder(
				FileUtils.replaceExtensions(file.getName()),
				FileUtils.getParentDirPath(file),
				LightningProviders.inputStreamProvider()
		);
	}

	public static LightningBuilder fromDirectory(final File file) {
		Valid.notNull(file, "File mustn't be null");
		Valid.checkBoolean(!file.getName().contains("."), "File-Name mustn't contain '.'");

		if (!file.exists()) {
			file.mkdirs();
		}

		//Will return the name of the folder as default name
		return new LightningBuilder(file.getName(), file.getAbsolutePath(), LightningProviders.inputStreamProvider());
	}

	// ----------------------------------------------------------------------------------------------------
	// Adding out settings
	// ----------------------------------------------------------------------------------------------------

	public LightningBuilder addInputStreamFromFile(final File file) {
		Valid.notNull(file, "File mustn't be null");

		inputStream = FileUtils.createInputStream(file);
		return this;
	}

	public LightningBuilder addInputStreamFromResource(final String resource) {
		inputStream = inputStreamProvider.createInputStreamFromInnerResource(resource);

		Valid.notNull(inputStream, "InputStream is null.", "No inbuilt resource '" + resource + "' found: ");
		return this;
	}

	public LightningBuilder setName(final String name) {
		Valid.notNull(name, "Name mustn't be null.");

		this.name = name;
		return this;
	}

	public LightningBuilder addInputStream(final InputStream inputStream) {
		Valid.notNull(inputStream, "InputStream mustn't be null");

		this.inputStream = inputStream;
		return this;
	}

	public LightningBuilder setConfigSettings(final ConfigSettings configSettings) {
		Valid.notNull(configSettings, "ConfigSettings mustn't be null");

		this.configSettings = configSettings;
		return this;
	}

	public LightningBuilder setReloadSettings(final ReloadSettings reloadSettings) {
		Valid.notNull(reloadSettings, "ReloadSettings mustn't be null");

		this.reloadSettings = reloadSettings;
		return this;
	}

	public LightningBuilder setDataType(final DataType dataType) {
		Valid.notNull(dataType, "DataType mustn't be null");

		this.dataType = dataType;
		return this;
	}

	// ----------------------------------------------------------------------------------------------------
	// Create the objects of our FileTypes
	// ----------------------------------------------------------------------------------------------------

	public LightningFile createLightningFile() {
		return new LightningFile(name, path, inputStream, reloadSettings, configSettings);
	}

	public Config createConfig() {
		return new Config(name, path, inputStream, reloadSettings, configSettings, dataType);
	}

	public Yaml createYaml() {
		return new Yaml(name, path, inputStream, reloadSettings, configSettings, dataType);
	}

	public Toml createToml() {
		return new Toml(name, path, inputStream, reloadSettings);
	}

	public Json createJson() {
		return new Json(name, path, inputStream, reloadSettings);
	}
}

