package de.leonhard.storage.internal.datafiles.config;

import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.datafiles.section.LightningConfigSection;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
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
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getHeader(this.fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(@Nullable final List<String> header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, header, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getFooter(fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(@Nullable final List<String> footer) {
		this.update();

		if (getCommentSetting() != Comment.SKIP) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, footer, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getHeader(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getHeader(this.fileData, key, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		Valid.notNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, key, header, this.getDataType(), this.getCommentSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getFooter(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getFooter(this.fileData, key, getDataType(), getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		Valid.notNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, key, footer, this.getDataType(), this.getCommentSetting());
			if (!fileData.toMap().equals(tempMap)) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public LightningConfigSection getSection(@NotNull final String sectionKey) {
		return new LightningConfigSection(this, sectionKey);
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