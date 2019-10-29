package de.leonhard.storage.internal.datafiles.config;

import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.LightningUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended LightningFile with added methods for Config purposes
 */
@SuppressWarnings("unused")
public class LightningConfig extends LightningFile {

	public LightningConfig(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final DataType dataType) {
		super(file, inputStream, reloadSetting, (configSetting == null ? ConfigSetting.PRESERVE_COMMENTS : configSetting), dataType);
	}


	public List<String> getHeader() {
		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, getDataType(), getConfigSetting());
		}
	}

	public void setHeader(@Nullable final List<String> header) {
		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, header, getDataType(), getConfigSetting());
			if (!this.fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	public List<String> getFooter() {
		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getFooter(fileData, getDataType(), getConfigSetting());
		}
	}

	public void setFooter(@Nullable final List<String> footer) {
		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, footer, getDataType(), getConfigSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	public List<String> getHeader(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null || this.getPathPrefix().isEmpty()) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, finalKey, getDataType(), getConfigSetting());
		}
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null || this.getPathPrefix().isEmpty()) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, finalKey, header, getDataType(), getConfigSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	public List<String> getFooter(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null || this.getPathPrefix().isEmpty()) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getFooter(this.fileData, finalKey, getDataType(), getConfigSetting());
		}
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null || this.getPathPrefix().isEmpty()) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, finalKey, footer, getDataType(), getConfigSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	protected final LightningConfig getLightningConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningConfig config = (LightningConfig) obj;
			return super.equals(config.getLightningFileInstance());
		}
	}
}