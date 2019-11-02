package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.settings.Comment;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public abstract class CommentEnabledFile extends FlatFile {

	@Getter
	@Setter
	private Comment commentSetting = Comment.SKIP;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileType fileType) {
		super(file, fileType);
	}

	public void reload(@NotNull final Comment commentSetting) {
		this.setCommentSetting(commentSetting);
		this.reload();
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		this.setCommentSetting(commentSetting);
		this.set(key, value);
	}

	public synchronized void remove(@NotNull final String key, @NotNull final Comment commentSetting) {
		this.setCommentSetting(commentSetting);
		this.remove(key);
	}
}