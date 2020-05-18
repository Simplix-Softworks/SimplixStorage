package de.leonhard.storage.util;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Valid {

  public void error(final Throwable cause, final String... messages) {
    throw new LightningValidationException(cause, messages);
  }

  public void error(final String... errorMessage) {
    throw new LightningValidationException(errorMessage);
  }

  public void checkBoolean(final boolean condition) {
    checkBoolean(condition, "Valid.checkBoolean(): Condition is False.");
  }

  public void checkBoolean(final boolean condition, final String... errorMessage) {
    if (!condition) {
      throw new LightningValidationException(errorMessage);
    }
  }

  public <T> void notNull(@Nullable final T object) {
    notNull(object, "Valid.notNull(): Validated Object is null");
  }

  public <T> void notNull(@Nullable final T object, @Nullable final String... message) {
    if (object != null) {
      return;
    }
    throw new LightningValidationException(message);
  }
}
