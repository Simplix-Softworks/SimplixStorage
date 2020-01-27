package de.leonhard.storage.internal.exception;

public class LightningValidationException extends RuntimeException {
	private final long serialVersionUID = -7961367314553460325L;

	public LightningValidationException(final Throwable throwable, final String... messages) {
		super(throwable);

		for (final String part : messages) {
			System.err.println(part);
		}
	}

	public LightningValidationException(final String... messages) {

		for (final String part : messages) {
			System.err.println(part);
		}
	}
}
