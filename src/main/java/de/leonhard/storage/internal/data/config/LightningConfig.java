package de.leonhard.storage.internal.data.config;

import de.leonhard.storage.internal.base.interfaces.CommentSettingBase;
import de.leonhard.storage.internal.base.interfaces.ConfigBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.base.interfaces.ReloadSettingBase;
import de.leonhard.storage.internal.data.raw.LightningFile;
import de.leonhard.storage.internal.data.section.LightningConfigSection;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.utils.basic.Objects;
import de.leonhard.storage.internal.utils.datafiles.LightningUtils;
import de.leonhard.storage.internal.utils.editor.LightningEditor;
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
public class LightningConfig extends LightningFile implements ConfigBase {

	protected LightningConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getHeader(this.fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, header, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	@Override
	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getFooter(fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, footer, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getHeader(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getHeader(this.fileData, key, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setHeader(this.fileData, key, header, this.getDataType(), this.getCommentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getFooter(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getFooter(this.fileData, key, getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = LightningUtils.setFooter(this.fileData, key, footer, this.getDataType(), this.getCommentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				LightningEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	@Override
	public List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getComments(this.fileData, this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getComments(this.fileData, key, this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getComments(this.fileData, this.getDataType(), this.getCommentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return LightningUtils.getComments(this.fileData, key, this.getDataType(), this.getCommentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public LightningConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	protected final LightningConfig getLightningConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningConfig config = (LightningConfig) obj;
			return super.equals(config.getLightningFileInstance());
		}
	}


	private static class LocalSection extends LightningConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull LightningConfig lightningConfig) {
			super(sectionKey, lightningConfig);
		}
	}
}