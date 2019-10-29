package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.enums.Comments;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


public abstract class CommentEnabledFile extends FlatFile {


	@Getter
	@Setter
	private Comments commentSetting = Comments.SKIP;

	protected CommentEnabledFile(final @NotNull File file, final @NotNull FileType fileType) {
		super(file, fileType);
	}
}