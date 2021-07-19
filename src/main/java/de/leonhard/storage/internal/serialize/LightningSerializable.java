package de.leonhard.storage.internal.serialize;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface LightningSerializable<T> {

  /**
   * Get our serializable from data in data-structure.
   *
   * @param obj Data to deserialize our class from.
   * @throws ClassCastException Exception thrown when deserialization failed.
   */
  @NotNull T deserialize(@NonNull final Object obj) throws ClassCastException;

  /**
   * Save our serializable to data-structure.
   *
   * @throws ClassCastException Exception thrown when serialization failed.
   * @param t
   */
  @NotNull Object serialize(final Object t) throws ClassCastException;

  @NotNull Class<T> getClazz();
}
