package de.leonhard.storage.lightningstorage.internal.datafiles.config;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.datafiles.raw.TomlFile;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class TomlConfig extends TomlFile {

	public TomlConfig(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		super(file, inputStream, reloadSetting, configSetting == null ? ConfigSetting.PRESERVE_COMMENTS : configSetting, fileDataType);
	}
}