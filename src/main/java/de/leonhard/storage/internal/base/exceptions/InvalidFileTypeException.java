package de.leonhard.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class InvalidFileTypeException extends LightningException {

	public InvalidFileTypeException(String errorMessage) {
		super(errorMessage);
	}

	public InvalidFileTypeException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}