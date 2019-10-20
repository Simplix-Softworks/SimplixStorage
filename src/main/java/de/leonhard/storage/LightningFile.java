package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import java.io.File;
import java.io.InputStream;


public class LightningFile extends de.leonhard.storage.internal.datafiles.LightningFile {

	public LightningFile(final String name, final String path) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), null, null);
	}

	public LightningFile(final String name, final String path, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		super(new File(path, FileTypeUtils.addExtension(name, FileType.LIGHTNING)), inputStream, reloadSettings);
	}

	public LightningFile(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		super(file, inputStream, reloadSettings);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}