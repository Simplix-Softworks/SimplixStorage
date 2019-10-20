package de.leonhard.storage.internal.datafiles.config;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import de.leonhard.storage.internal.enums.ReloadSetting;
import java.io.File;
import java.io.InputStream;


public class JsonConfig extends JsonFile {

	public JsonConfig(@NotNull File file, @Nullable InputStream inputStream, @Nullable ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}
}