package de.leonhard.storage.internal.exception;

public class LightningValidationException extends RuntimeException {
	private final long serialVersionUID = -7961367314553460325L;

	public LightningValidationException(final String... message) {

		for (final String part : message) {
			System.err.println(part);
		}
	}
}
