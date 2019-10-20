package de.leonhard.storage;

import de.leonhard.storage.internal.base.enums.FileType;
import de.leonhard.storage.internal.base.enums.ReloadSetting;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import de.leonhard.storage.internal.utils.FileTypeUtils;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class Json extends JsonFile {

	public Json(final File file) throws InvalidFileTypeException {
		super(file, null, null);
	}

	public Json(final String name, final String path) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.JSON)), null, null);
	}

	public Json(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}