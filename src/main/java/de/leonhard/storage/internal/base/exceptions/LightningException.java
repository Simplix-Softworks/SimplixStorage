package de.leonhard.storage.internal.base.exceptions;

@SuppressWarnings("WeakerAccess")
public abstract class LightningException extends RuntimeException {

	public LightningException(final String errorMessage) {
		super(errorMessage);
	}

	public LightningException(final String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}