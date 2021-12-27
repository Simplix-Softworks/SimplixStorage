package de.leonhard.storage.internal.exceptions;

import de.leonhard.storage.internal.exception.SimplixException;

/**
 * Thrown to indicate that something went wrong, or just to end our code
 */
public class SimplixValidationException extends SimplixException {

  private static final long serialVersionUID = -7961367314553460325L;

  public SimplixValidationException(
      final Throwable throwable,
      final String... messages) {
    super(throwable, messages);
  }

  public SimplixValidationException(final String... messages) {
    super(messages);
  }
}
