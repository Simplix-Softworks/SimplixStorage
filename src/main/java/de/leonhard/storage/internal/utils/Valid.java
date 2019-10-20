package de.leonhard.storage.internal.utils;

import de.leonhard.storage.internal.base.exceptions.ObjectIsNullException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Valid {

	public static <T> void notNull(final T object) {
		if (object != null) {
			return;
		}
		throw new ObjectIsNullException("Validated Object is null");
	}

	public static <T> void notNull(final T object, final String message) {
		if (object != null) {
			return;
		}
		throw new ObjectIsNullException(message);
	}
}