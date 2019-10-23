package de.leonhard.storage.lightningstorage.internal.datafiles.config;

import de.leonhard.storage.lightningstorage.editor.LightningEditor;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.lightningstorage.utils.LightningUtils;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningConfig extends LightningFile {

	public LightningConfig(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		super(file, inputStream, reloadSetting, (configSetting == null ? ConfigSetting.PRESERVE_COMMENTS : configSetting), fileDataType);
	}


	public List<String> getHeader() {
		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, getFileDataType(), getConfigSetting());
		}
	}

	public void setHeader(@Nullable final List<String> header) {
		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, header, getFileDataType(), getConfigSetting());
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
			return LightningUtils.getFooter(fileData, getFileDataType(), getConfigSetting());
		}
	}

	public void setFooter(@NotNull final List<String> footer) {
		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, footer, getFileDataType(), getConfigSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	public List<String> getHeader(@NotNull final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, finalKey, getFileDataType(), getConfigSetting());
		}
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, finalKey, header, getFileDataType(), getConfigSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getConfigSetting());
			}
		}
	}

	public List<String> getFooter(@NotNull final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getFooter(this.fileData, finalKey, getFileDataType(), getConfigSetting());
		}
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (getConfigSetting() != ConfigSetting.SKIP_COMMENTS) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, finalKey, footer, getFileDataType(), getConfigSetting());
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