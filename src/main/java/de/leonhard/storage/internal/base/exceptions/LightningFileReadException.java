package de.leonhard.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class LightningFileReadException extends Exception {

	public LightningFileReadException(String errorMessage) {
		super(errorMessage);
	}

	public LightningFileReadException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}