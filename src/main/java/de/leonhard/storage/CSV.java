package de.leonhard.storage;

import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.CSVFile;
import de.leonhard.storage.internal.enums.ReloadSetting;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class CSV extends CSVFile {

	public CSV(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}