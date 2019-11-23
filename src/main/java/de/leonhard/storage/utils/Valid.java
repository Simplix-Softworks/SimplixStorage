package de.leonhard.storage.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Valid {

	public void checkBoolean(boolean condition) {
		checkBoolean(condition, "Valid.checkBoolean(): Condition is False.");
	}

	public void checkBoolean(boolean condition, String... errorMessage) {
		if (!condition) {
            throw new LightningException(errorMessage);
        }
	}

	public <T> void notNull(T object) {
		if (object != null) {
			return;
		}
		throw new LightningException("Valid.notNull(): Validated Object is null");
	}

	public <T> void notNull(T object, String... message) {
		if (object != null) {
			return;
		}
		throw new LightningException(message);
	}

	private class LightningException extends RuntimeException {
		private final long serialVersionUID = -7961367314553460325L;

		private LightningException(String... message) {
			for (String part : message) {
				System.out.println(part);
			}
		}
	}
}
