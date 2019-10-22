package de.leonhard.storage.lightningstorage.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Valid {

	public static <T> void notNull(@Nullable final T object) {
		if (object != null) {
			return;
		}
		throw new IllegalStateException("Validated Object may not be null");
	}

	public static <T> void notNull(@Nullable final T object, @NotNull final String message) {
		if (object != null) {
			return;
		}
		throw new IllegalArgumentException(message);
	}
}