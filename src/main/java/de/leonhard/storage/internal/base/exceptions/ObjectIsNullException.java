package de.leonhard.storage.internal.base.exceptions;

public class ObjectIsNullException extends LightningException {

	public ObjectIsNullException(String errorMessage) {
		super(errorMessage);
	}

	public ObjectIsNullException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}