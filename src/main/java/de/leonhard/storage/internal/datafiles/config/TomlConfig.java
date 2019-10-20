package de.leonhard.storage.internal.datafiles.config;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import de.leonhard.storage.internal.enums.ReloadSetting;
import java.io.File;
import java.io.InputStream;


public class TomlConfig extends TomlFile {

	public TomlConfig(@NotNull File file, @Nullable InputStream inputStream, @Nullable ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
	}
}