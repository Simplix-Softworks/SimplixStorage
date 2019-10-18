package de.leonhard.storage.internal.base.exceptions;

@SuppressWarnings("unused")
public class InvalidSettingException extends Exception {


	public InvalidSettingException(String errorMessage) {
		super(errorMessage);
	}

	public InvalidSettingException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}