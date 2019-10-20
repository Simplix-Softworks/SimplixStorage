package de.leonhard.storage.internal.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Valid {

	public static <T> void notNull(final T object) {
		if (object != null) {
			return;
		}
		throw new LightningException("Validated Object is null");
	}

	public static <T> void notNull(final T object, final String... message) {
		if (object != null) {
			return;
		}
		throw new LightningException(message);
	}

	private static class LightningException extends RuntimeException {

		private LightningException(final String... message) {
			for (String part : message) {
				System.out.println(part);
			}
		}
	}
}