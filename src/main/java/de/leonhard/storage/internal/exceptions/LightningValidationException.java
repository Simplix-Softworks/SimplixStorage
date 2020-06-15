package de.leonhard.storage.internal.exceptions;

import de.leonhard.storage.internal.exception.LightningException;

/**
 * Thrown to indicate that something went wrong, or just to end our code
 */
public class LightningValidationException extends LightningException {

  private final long serialVersionUID = -7961367314553460325L;

  public LightningValidationException(
      final Throwable throwable,
      final String... messages) {
    super(throwable, messages);
  }

  public LightningValidationException(final String... messages) {
    super(messages);
  }
}
