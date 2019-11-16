package de.leonhard.storage;

import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.Valid;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public class LightningBuilder {
	private final String path;
	private String name;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;
	private ConfigSettings configSettings;

	private LightningBuilder(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public static LightningBuilder fromPath(String name, String path) {
		Valid.notNull(name, "Name mustn't be null");
		return new LightningBuilder(name, path);
	}

	public static LightningBuilder fromPath(final Path path) {
		return fromFile(path.toFile());
	}

	public static LightningBuilder fromFile(File file) {
		Valid.notNull(file, "File mustn't be null");
		String path = "";
		if (file.isDirectory()) {
			path = file.getAbsolutePath();
		} else {
			path = file.getParentFile().getAbsolutePath();
		}
		return new LightningBuilder(FileUtils.replaceExtensions(file.getName()), path);
	}

	public LightningBuilder addInputStreamFromFile(File file) {
		Valid.notNull(file, "File mustn't be null");
		this.inputStream = FileUtils.createInputStream(file);
		return this;
	}

	public LightningBuilder addInputStreamFromResource(String resource) {
		this.inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		Valid.notNull(inputStream, "InputStream is null.", "No inbuilt resource '" + resource + "' found: ");
		return this;
	}

	public LightningBuilder setName(String name) {
		Valid.notNull(name, "Name mustn't be null.");
		this.name = name;
		return this;
	}

	public LightningBuilder addInputStream(InputStream inputStream) {
		Valid.notNull(inputStream, "InputStream mustn't be null");
		this.inputStream = inputStream;
		return this;
	}

	public LightningBuilder setConfigSettings(ConfigSettings configSettings) {
		Valid.notNull(configSettings, "ConfigSettings mustn't be null");

		this.configSettings = configSettings;
		return this;
	}

	public LightningBuilder setReloadSettings(ReloadSettings reloadSettings) {
		Valid.notNull(reloadSettings, "ReloadSettings mustn't be null");

		this.reloadSettings = reloadSettings;
		return this;
	}

	public LightningFile createLightningFile() {
		return new LightningFile(name, path, inputStream, reloadSettings, configSettings);
	}


	// ----------------------------------------------------------------------------------------------------
	// Create the objects of our FileTypes
	// ----------------------------------------------------------------------------------------------------

	public Config createConfig() {
		return new Config(name, path, inputStream, reloadSettings, configSettings);
	}

	public Yaml createYaml() {
		return new Yaml(name, path, inputStream, reloadSettings, configSettings);
	}

	public Toml createToml() {
		return new Toml(name, path);
	}

	public Json createJson() {
		return new Json(name, path, reloadSettings);
	}
}

