package de.leonhard.storage.internal.provider;

import de.leonhard.storage.internal.exceptions.SimplixValidationException;
import lombok.NonNull;

public abstract class ExceptionHandler {

  public RuntimeException create(
      @NonNull final Throwable throwable,
      @NonNull final String... messages) {
    return new SimplixValidationException(
        throwable,
        messages);
  }
}
