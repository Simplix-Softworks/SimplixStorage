package de.leonhard.storage.internal.exception;

public class LightningFileException extends RuntimeException {

	private static final long serialVersionUID = 781871632995757470L;

	public LightningFileException(final String... message) {

		for (final String part : message) {
			System.err.println(part);
		}
	}
}
