package de.leonhard.storage.internal.exceptions;

import de.leonhard.storage.internal.exception.LightningException;

/**
 * Thrown when a problem occurs while processing our
 */
public class LightningFileException extends LightningException {

  private static final long serialVersionUID = 781871632995757470L;

  public LightningFileException(final Throwable throwable, final String... messages) {
    super(throwable, messages);
  }

  public LightningFileException(final String... messages) {
    super(messages);
  }
}
