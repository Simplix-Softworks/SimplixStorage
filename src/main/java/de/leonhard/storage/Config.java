package de.leonhard.storage;

import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.utils.FileTypeUtils;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Config extends YamlConfig {

	public Config(final String name, final String path) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), null, null, null, null);
	}

	public Config(final String name, final String path, final InputStream inputStream) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, null, null, null);
	}

	public Config(final String name, final String path, final InputStream inputStream, final ReloadSetting reloadSetting) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSetting, null, null);
	}

	public Config(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) {
		super(file, inputStream, reloadSetting, null, null);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}