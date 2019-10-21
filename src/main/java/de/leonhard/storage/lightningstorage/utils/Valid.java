package de.leonhard.storage.lightningstorage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Valid {

	public static <T> void notNull(final T object) {
		if (object != null) {
			return;
		}
		throw new IllegalStateException("Validated Object may not be null");
	}

	public static <T> void notNull(final T object, final String message) {
		if (object != null) {
			return;
		}
		throw new IllegalArgumentException(message);
	}
}