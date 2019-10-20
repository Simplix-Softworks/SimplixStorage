package de.leonhard.storage.internal.datafiles.config;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.ReloadSetting;
import java.io.File;
import java.io.InputStream;


public class LightningConfig extends LightningFile {

	public LightningConfig(@NotNull File file, @Nullable InputStream inputStream, @Nullable ReloadSetting reloadSetting) throws InvalidFileTypeException {
		super(file, inputStream, reloadSetting);
		setConfigSetting(ConfigSetting.PRESERVE_COMMENTS);
	}
}