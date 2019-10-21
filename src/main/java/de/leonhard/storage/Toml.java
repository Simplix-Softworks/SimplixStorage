package de.leonhard.storage;

import de.leonhard.storage.lightningstorage.internal.datafiles.raw.TomlFile;
import de.leonhard.storage.lightningstorage.utils.FileTypeUtils;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Toml extends TomlFile {

	public Toml(final File file) {
		super(file, null, null, null, null);
	}

	public Toml(final String name, final String path) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.TOML)), null, null, null, null);
	}

	public Toml(final String name, final String path, final ReloadSetting reloadSetting) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.TOML)), null, reloadSetting, null, null);
	}

	public Toml(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) {
		super(file, inputStream, reloadSetting, null, null);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}