package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.config.YamlConfig;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Config extends YamlConfig {

	public Config(final String name, final String path) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), null, null);
	}

	public Config(final String name, final String path, final InputStream inputStream) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, null);
	}

	public Config(final String name, final String path, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSetting);
	}

	public Config(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}