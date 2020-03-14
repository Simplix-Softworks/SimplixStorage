package de.leonhard.storage.internal.serialize;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Class to register serializable's */
@UtilityClass
public class LightningSerializer {
  private final List<LightningSerializable<?>> serializes =
      Collections.synchronizedList(new ArrayList<>());

  /**
   * Register a serializable to our list
   *
   * @param lightningSerializable Serializable to register
   */
  public void registerSerializable(@NonNull final LightningSerializable<?> lightningSerializable) {
    Valid.notNull(lightningSerializable.getClazz(), "Class of serializable mustn't be null");
    serializes.add(lightningSerializable);
  }

  public LightningSerializable<?> findSerializable(final Class<?> clazz) {
    for (final LightningSerializable<?> serializable : serializes) {
      if (serializable.getClazz().equals(clazz)) {
        return serializable;
      }
    }
    return null;
  }

  @SuppressWarnings("ALL")
  public <T> T serialize(final Object obj, final Class<T> clazz) {
    final LightningSerializable serializable = findSerializable(clazz);
    Valid.notNull(serializable, "No serializable found for '" + clazz.getSimpleName() + "'");
    return (T) serializable.serialize(obj);
  }

  public Object deserialize(final Object obj) {
    final LightningSerializable<?> serializable = findSerializable(obj.getClass());
    Valid.notNull(
        serializable, "No serializable found for '" + obj.getClass().getSimpleName() + "'");
    return serializable.deserialize(obj);
  }
}
