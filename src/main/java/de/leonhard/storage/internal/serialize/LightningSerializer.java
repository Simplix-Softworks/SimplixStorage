package de.leonhard.storage.internal.serialize;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to register serializable's
 */
@UtilityClass
public class LightningSerializer {

  private final List<LightningSerializable<?>> serializables = Collections
      .synchronizedList(new ArrayList<>());

  @SuppressWarnings("unused")
  public boolean isSerializable(final Class<?> clazz) {
    return findSerializable(clazz) != null;
  }

  /**
   * Register a serializable to our list
   *
   * @param serializable Serializable to register
   */
  @SuppressWarnings("unused")
  public void registerSerializable(@NonNull final LightningSerializable<?> serializable) {
    Valid.notNull(
        serializable.getClazz(),
        "Class of serializable mustn't be null");
    serializables.add(serializable);
  }

  @Nullable
  public LightningSerializable<?> findSerializable(final Class<?> clazz) {
    for (val serializable : serializables) {
      if (serializable.getClazz().equals(clazz)) {
        return serializable;
      }
    }
    return null;
  }

  /**e
   * Method to save an object
   */
  public @NotNull Object serialize(final @NotNull Object obj) {
    val serializable = findSerializable(obj.getClass());

    Valid.notNull(
        serializable,
        "No serializable found for '" + obj.getClass().getSimpleName() + "'");
    assert serializable != null;
    return serializable.serialize(obj);
  }

  @SuppressWarnings("unchecked")
  public <T> @NotNull T deserialize(final @NotNull Object raw, @NotNull Class<T> type) {
    val serializable = findSerializable(type);
    Valid.notNull(
        serializable,
        "No serializable found for '" + type.getSimpleName() + "'",
        "Raw: '" + raw.getClass().getSimpleName() + "'");
    assert serializable != null;
    return (T) serializable.deserialize(raw);
  }
}
