package de.leonhard.storage;

import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.Valid;

import java.io.File;
import java.io.InputStream;


public class LightningBuilder {

	private final String path;
	private String name;
	private InputStream inputStream;
	private ReloadSettings reloadSettings;


	private LightningBuilder(final String name, final String path) {
		this.name = name;
		this.path = path;
	}

	public static LightningBuilder fromPath(final String name, final String path) {
		Valid.notNull(name, "Name mustn't be null");
		return new LightningBuilder(name, path);
	}

	public static LightningBuilder fromFile(final File file) {
		String path = "";
		if (file.isDirectory()) {
			path = file.getAbsolutePath();
		} else {
			path = file.getParentFile().getAbsolutePath();
		}
		return new LightningBuilder(FileUtils.replaceExtensions(file.getName()), path);
	}


	public LightningBuilder addInputStreamFromResource(final String resource) {
		this.inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		Valid.notNull(inputStream, "No inbuild resource '" + resource + "'", "InputStream is null.");
		return this;
	}

	public LightningBuilder setName(final String name) {
		Valid.notNull(name, "Name mustn't be null.");
		this.name = name;
		return this;
	}

	public LightningBuilder addInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public LightningBuilder setReloadSettings(final ReloadSettings reloadSettings) {
		this.reloadSettings = reloadSettings;
		return this;
	}

	public Config createConfig() {
		return new Config(name, path, inputStream, reloadSettings);
	}

	public Yaml createYaml() {
		return new Yaml(name, path, inputStream, reloadSettings);
	}

	public Toml createToml() {
		return new Toml(name, path);
	}

	public Json createJson() {
		return new Json(name, path, reloadSettings);
	}
}

