package de.leonhard.storage.internal.datafiles.config;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class JsonConfig extends JsonFile {

	public JsonConfig(@NotNull File file, @Nullable InputStream inputStream, @Nullable ReloadSetting reloadSetting, @Nullable ConfigSetting configSetting, @Nullable FileData.Type dataType) {
		super(file, inputStream, reloadSetting, configSetting, dataType);
	}
}