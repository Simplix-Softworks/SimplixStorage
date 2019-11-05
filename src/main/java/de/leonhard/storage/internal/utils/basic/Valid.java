package de.leonhard.storage.internal.utils.basic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to check validities
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Valid {

	/**
	 * Checks if given Object is null
	 */
	public static <T> void notNull(final @Nullable T object) {
		if (object != null) {
			return;
		}
		throw new IllegalStateException("Validated Object must not be null");
	}

	public static <T> void notNull(final @Nullable T object, final @NotNull String message) {
		if (object != null) {
			return;
		}
		throw new IllegalArgumentException(message);
	}


	/**
	 * Returns the given Object if not null
	 */
	public static <T> T notNullObject(final @Nullable T object) {
		if (object != null) {
			return object;
		}
		throw new IllegalStateException("Validated Object must not be null");
	}

	public static <T> T notNullObject(final @Nullable T object, final @NotNull String message) {
		if (object != null) {
			return object;
		}
		throw new IllegalArgumentException(message);
	}
}