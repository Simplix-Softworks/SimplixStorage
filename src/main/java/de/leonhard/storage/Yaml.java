package de.leonhard.storage;

import de.leonhard.storage.internal.base.enums.FileType;
import de.leonhard.storage.internal.base.enums.ReloadSetting;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.utils.FileTypeUtils;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Yaml extends YamlFile {

	public Yaml(final String name, final String path) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), null, null);
	}

	public Yaml(final String name, final String path, final InputStream inputStream) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, null);
	}

	public Yaml(final String name, final String path, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.YAML)), inputStream, reloadSetting);
	}

	public Yaml(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}