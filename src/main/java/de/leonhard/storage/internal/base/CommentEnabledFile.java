package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.utils.basic.Valid;
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
		this.setPreserveComments(Valid.notNullObject(preserveComments, "PreserveComments must not be null"));
		this.reload();
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final boolean preserveComments) {
		this.setPreserveComments(Valid.notNullObject(preserveComments, "PreserveComments must not be null"));
		this.set(Valid.notNullObject(key, "Key must not be null"), Valid.notNullObject(value, "Value must not be null"));
	}

	public synchronized void remove(final @NotNull String key, final boolean preserveComments) {
		this.setPreserveComments(Valid.notNullObject(preserveComments, "PreserveComments must not be null"));
		this.remove(Valid.notNullObject(key, "Key must not be null"));
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			CommentEnabledFile commentEnabledFile = (CommentEnabledFile) obj;
			return this.preserveComments == commentEnabledFile.preserveComments
				   && super.equals(commentEnabledFile);
		}
	}
}