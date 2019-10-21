package de.leonhard.storage;

import de.leonhard.storage.lightningstorage.internal.datafiles.raw.CSVFile;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("unused")
public class CSV extends CSVFile {

	public CSV(final File file, final InputStream inputStream, final ReloadSetting reloadSetting) {
		super(file, inputStream, reloadSetting, null, null);
	}

	public boolean contains(final String key) {
		return hasKey(key);
	}
}