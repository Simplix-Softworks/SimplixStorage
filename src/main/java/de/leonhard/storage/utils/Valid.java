package de.leonhard.storage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Valid {


	public static void checkBoolean(boolean condition) {
		checkBoolean(condition, "Valid.checkBoolean(): Condition is False.");
	}

	public static void checkBoolean(boolean condition, String... errorMessage) {
		if (!condition)
			throw new LightningException(errorMessage);
	}

	public static <T> void notNull(T object) {
		if (object != null) {
			return;
		}
		throw new LightningException("Validated Object is null");
	}

	public static <T> void notNull(T object, String... message) {
		if (object != null) {
			return;
		}
		throw new LightningException(message);
	}

	private static class LightningException extends RuntimeException {
		private static final long serialVersionUID = -7961367314553460325L;

		private LightningException(String... message) {
			for (String part : message) {
				System.out.println(part);
			}
		}
	}
}
