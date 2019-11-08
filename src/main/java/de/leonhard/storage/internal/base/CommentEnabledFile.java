package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.base.interfaces.CommentBase;
import de.leonhard.storage.internal.base.interfaces.FileTypeBase;
import de.leonhard.storage.internal.settings.Comment;
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
	private CommentBase commentSetting = Comment.SKIP;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileTypeBase fileType) {
		super(file, fileType);
	}

	public void reload(final @NotNull Comment commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.reload();
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull CommentBase commentSetting) {
		this.setCommentSetting(Objects.notNull(commentSetting, "CommentSetting must not be null"));
		this.set(Objects.notNull(key, "Key must not be null"), Objects.notNull(value, "Value must not be null"));
	}

	public synchronized void remove(final @NotNull String key, final @NotNull CommentBase commentSetting) {
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