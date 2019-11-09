package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.base.interfaces.CommentSettingBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.base.interfaces.FileTypeBase;
import de.leonhard.storage.internal.base.interfaces.ReloadSettingBase;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public abstract class CommentEnabledFile extends FlatFile {

	@Getter
	@Setter
	private CommentSettingBase commentSetting = Comment.SKIP;
	@Getter
	@Setter
	private DataTypeBase dataType = DataType.AUTOMATIC;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileTypeBase fileType, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, fileType, reloadSetting);
		if (commentSetting != null) {
			this.setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			this.setDataType(dataType);
		}
	}

	public void reload(final @NotNull Comment commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(Objects.notNull(key, "Key must not be null"), Objects.notNull(value, "Value must not be null"));
	}

	public synchronized void remove(final @NotNull String key, final @NotNull CommentSettingBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.remove(Objects.notNull(key, "Key must not be null"));
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			CommentEnabledFile commentEnabledFile = (CommentEnabledFile) obj;
			return this.commentSetting == commentEnabledFile.commentSetting
				   && super.equals(commentEnabledFile);
		}
	}
}