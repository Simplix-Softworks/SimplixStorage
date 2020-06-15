package de.leonhard.storage.internal.exception;

import lombok.NonNull;

/**
 * Every exception which is thrown in LightningStorage internally extends this exception.
 * <p>
 * It describes the basic format of exceptions we use. See implementations in {@link
 * de.leonhard.storage.internal.exceptions}
 */
public class LightningException extends RuntimeException {

  private static final long serialVersionUID = 4815788455395994435L;

  protected LightningException(
      @NonNull final Throwable throwable,
      @NonNull final String... messages) {
    super(String.join("\n", messages), throwable, false, true);
  }

  protected LightningException(final String... messages) {
    super(String.join("\n", messages), null, false, true);
  }

  protected LightningException(
      @NonNull final Throwable cause,
      @NonNull final boolean enableSuppression,
      @NonNull final boolean writableStackTrace,
      @NonNull final String... messages) {
    super(String.join("\n", messages), cause, enableSuppression, writableStackTrace);
  }
}
