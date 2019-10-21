package de.leonhard.storage;

import de.leonhard.storage.lightningstorage.internal.datafiles.raw.JsonFile;
import de.leonhard.storage.lightningstorage.utils.FileTypeUtils;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Json extends JsonFile {

	public Json(final File file) {
		super(file, null, null, null, null);
	}

	public Json(final String name, final String path) {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), null, null, null, null);
	}

	public Json(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) {
		super(file, inputStream, reloadSetting, null, null);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}