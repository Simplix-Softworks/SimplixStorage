package de.leonhard.storage.internal.datafiles.config;

import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.datafiles.section.LightningConfigSection;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.enums.Comment;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.Reload;
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

	public LightningConfig(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final Reload reloadSetting, @Nullable final Comment commentSetting, @Nullable final DataType dataType) {
		super(file, inputStream, reloadSetting, (commentSetting == null ? Comment.PRESERVE : commentSetting), dataType);
	}


	public List<String> getHeader() {
		update();

		if (getCommentSetting().equals(Comment.SKIP)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, getDataType(), getCommentSetting());
		}
	}

	public void setHeader(@Nullable final List<String> header) {
		update();

		if (getCommentSetting() != Comment.SKIP) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, header, getDataType(), getCommentSetting());
			if (!this.fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getCommentSetting());
			}
		}
	}

	public List<String> getFooter() {
		update();

		if (getCommentSetting().equals(Comment.SKIP)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getFooter(fileData, getDataType(), getCommentSetting());
		}
	}

	public void setFooter(@Nullable final List<String> footer) {
		update();

		if (getCommentSetting() != Comment.SKIP) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, footer, getDataType(), getCommentSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getCommentSetting());
			}
		}
	}

	public List<String> getHeader(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (getCommentSetting().equals(Comment.SKIP)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getHeader(this.fileData, key, getDataType(), getCommentSetting());
		}
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (getCommentSetting() != Comment.SKIP) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, key, header, getDataType(), getCommentSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getCommentSetting());
			}
		}
	}

	public List<String> getFooter(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (getCommentSetting().equals(Comment.SKIP)) {
			return new ArrayList<>();
		} else {
			return LightningUtils.getFooter(this.fileData, key, getDataType(), getCommentSetting());
		}
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (getCommentSetting() != Comment.SKIP) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, key, footer, getDataType(), getCommentSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, getCommentSetting());
			}
		}
	}

	@Override
	public LightningConfigSection getSection(@NotNull final String key) {
		return new LightningConfigSection(this, key);
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