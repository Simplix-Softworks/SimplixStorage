package de.leonhard.storage.util;

import de.leonhard.storage.internal.exception.LightningValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Valid {

	public void checkBoolean(final boolean condition) {
		checkBoolean(condition, "Valid.checkBoolean(): Condition is False.");
	}

	public void checkBoolean(final boolean condition, final String... errorMessage) {
		if (!condition) {
			throw new LightningValidationException(errorMessage);
		}
	}

	public <T> void notNull(final T object) {
		notNull(object, "Valid.notNull(): Validated Object is null");
	}

	public <T> void notNull(final T object, final String... message) {
		if (object != null) {
			return;
		}
		throw new LightningValidationException(message);
	}
}
