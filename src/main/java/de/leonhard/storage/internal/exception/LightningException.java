package de.leonhard.storage.internal.exception;

import lombok.NonNull;

/**
 * Every exception which is thrown in LightningStorage internally extends this
 * exception.
 * <p>
 * It describes the basic format
 */
public class LightningException extends RuntimeException {

  private static final long serialVersionUID = 4815788455395994435L;

  protected LightningException(
      @NonNull final Throwable throwable,
      @NonNull final String... messages) {
    super(String.join("\n", messages), throwable, true, true);
  }

  protected LightningException(final String... messages) {
    super(String.join("\n", messages), null, true, false);
  }

  protected LightningException(
      @NonNull final Throwable cause,
      @NonNull final boolean enableSuppression,
      @NonNull final boolean writableStackTrace,
      @NonNull final String... messages) {
    super(String.join("\n", messages), cause, enableSuppression,
        writableStackTrace);
  }
}
