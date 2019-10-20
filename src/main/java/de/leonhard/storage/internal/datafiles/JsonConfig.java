package de.leonhard.storage.internal.datafiles;

import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.enums.ReloadSettings;
import java.io.File;
import java.io.InputStream;


@SuppressWarnings("WeakerAccess")
public class JsonConfig extends JsonFile {

	protected JsonConfig(File file, InputStream inputStream, ReloadSettings reloadSettings) throws InvalidFileTypeException {
		super(file, inputStream, reloadSettings);
	}
}