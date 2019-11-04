package de.leonhard.storage.internal.base;

import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public abstract class CommentEnabledFile extends FlatFile {

	@Getter
	@Setter
	private boolean preserveComments = true;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileType fileType) {
		super(file, fileType);
	}

	public void reload(final boolean preserveComments) {
		this.setPreserveComments(preserveComments);
		this.reload();
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, final boolean preserveComments) {
		this.setPreserveComments(preserveComments);
		this.set(key, value);
	}

	public synchronized void remove(@NotNull final String key, final boolean preserveComments) {
		this.setPreserveComments(preserveComments);
		this.remove(key);
	}
}