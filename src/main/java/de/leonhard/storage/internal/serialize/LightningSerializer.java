package de.leonhard.storage.internal.serialize;

import de.leonhard.storage.util.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Class to register serializable's
 */
@UtilityClass
public class LightningSerializer {

  private final List<LightningSerializable<?>> serializables = Collections.synchronizedList(new ArrayList<>());

  /**
   * Register a serializable to our list
   *
   * @param serializable Serializable to register
   */
  public void registerSerializable(@NonNull final LightningSerializable<?> serializable) {
    Valid.notNull(
        serializable.getClazz(),
        "Class of serializable mustn't be null");
    serializables.add(serializable);
  }

  public LightningSerializable<?> findSerializable(final Class<?> clazz) {
    for (final LightningSerializable<?> serializable : serializables) {
      if (serializable.getClazz().equals(clazz)) {
        return serializable;
      }
    }
    return null;
  }

  @SuppressWarnings("ALL")
  public <T> T serialize(final Object obj, final Class<T> clazz) {
    final LightningSerializable serializable = findSerializable(clazz);
    Valid.notNull(
        serializable,
        "No serializable found for '" + clazz.getSimpleName() + "'");
    return (T) serializable.serialize(obj);
  }

  public Object deserialize(final Object obj) {
    final LightningSerializable<?> serializable = findSerializable(obj.getClass());
    Valid.notNull(
        serializable,
        "No serializable found for '" + obj.getClass().getSimpleName() + "'");
    return serializable.deserialize(obj);
  }
}
