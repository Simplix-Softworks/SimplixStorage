package de.leonhard.storage.internal.exception;

/**
 * Thrown when a problem occur during parsing or writing NBT data.
 *
 * @author TheElectronWill
 */
public class TomlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TomlException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TomlException(final String message) {
		super(message);
	}
}