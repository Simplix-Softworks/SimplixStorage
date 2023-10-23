package de.leonhard.storage.internal;

import de.leonhard.storage.internal.provider.SimplixProviders;
import de.leonhard.storage.internal.serialize.SimplixSerializer;
import de.leonhard.storage.util.ClassWrapper;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public interface DataStorage {

  /**
   * Basic method to receive data from your data-structure
   *
   * @param key Key to search data for
   * @return Object in data-structure. Null if nothing was found!
   */
  @Nullable
  Object get(final String key);

  /**
   * Checks whether a key exists in the data-structure
   *
   * @param key Key to check
   * @return Returned value.
   */
  boolean contains(final String key);

  /**
   * Set an object to your data-structure
   *
   * @param key   The key your value should be associated with
   * @param value The value you want to set in your data-structure.
   */
  void set(final String key, final Object value);

  Set<String> singleLayerKeySet();

  Set<String> singleLayerKeySet(final String key);

  Set<String> keySet();

  Set<String> keySet(final String key);

  void remove(final String key);

  // ----------------------------------------------------------------------------------------------------
  //
  // Default-Implementations
  //
  // ----------------------------------------------------------------------------------------------------

  /**
   * Method to get a value of a predefined type from our data structure will return {@link
   * Optional#empty()} if the value wasn't found.
   *
   * @param key  Key to search the value for
   * @param type Type of the value
   */
  default <T> Optional<T> find(final String key, final Class<T> type) {
    final Object raw = get(key);
    //Key wasn't found
    if (raw == null) {
      return Optional.empty();
    }
    return Optional.of(ClassWrapper.getFromDef(raw, type));
  }

  /**
   * Method to deserialize a class using the {@link SimplixSerializer}. You will need to register
   * your serializable in the {@link SimplixSerializer} before.
   *
   * @param key   The key your value should be associated with.
   * @param value The value you want to set in your data-structure.
   */
  default <T> void setSerializable(@NonNull final String key, @NonNull final T value) {
    try {
      final Object data = SimplixSerializer.serialize(value);
      set(key, data);
    } catch (final Throwable throwable) {
      throw SimplixProviders.exceptionHandler().create(
          throwable,
          "Can't serialize: '" + key + "'",
          "Class: '" + value.getClass().getName() + "'",
          "Package: '" + value.getClass().getPackage() + "'");
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Getting Strings & primitive types from data-structure
  // ----------------------------------------------------------------------------------------------------

  /**
   * Get a value or a default one
   *
   * @param key Path to value in data-structure
   * @param def Default value & type of it
   */
  default <T> T get(final String key, final T def) {
    final Object raw = get(key);
    return raw == null ? def : ClassWrapper.getFromDef(raw, def);
  }

  /**
   * Get a String from a data-structure
   *
   * @param key Path to String in data-structure
   * @return Returns the value
   */
  default String getString(final String key) {
    return getOrDefault(key, "");
  }

  /**
   * Gets a long from a data-structure by key
   *
   * @param key Path to long in data-structure
   * @return String from data-structure
   */
  default long getLong(final String key) {
    return getOrDefault(key, 0L);
  }

  /**
   * Gets an int from a data-structure
   *
   * @param key Path to int in data-structure
   * @return Int from data-structure
   */
  default int getInt(final String key) {
    return getOrDefault(key, 0);
  }

  /**
   * Get a byte from a data-structure
   *
   * @param key Path to byte in data-structure
   * @return Byte from data-structure
   */
  default byte getByte(final String key) {
    return getOrDefault(key, (byte) 0);
  }

  /**
   * Get a boolean from a data-structure
   *
   * @param key Path to boolean in data-structure
   * @return Boolean from data-structure
   */
  default boolean getBoolean(final String key) {
    return getOrDefault(key, false);
  }

  /**
   * Get a float from a data-structure
   *
   * @param key Path to float in data-structure
   * @return Float from data-structure
   */
  default float getFloat(final String key) {
    return getOrDefault(key, 0F);
  }

  /**
   * Get a double from a data-structure
   *
   * @param key Path to double in the data-structure
   * @return Double from data-structure
   */
  default double getDouble(final String key) {
    return getOrDefault(key, 0D);
  }

  // ----------------------------------------------------------------------------------------------------
  // Getting Lists and non-ClassWrapper types from data-structure
  // ----------------------------------------------------------------------------------------------------

  /**
   * Get a List from a data-structure
   *
   * @param key Path to List in data structure.
   */
  default List<?> getList(final String key) {
    return getOrDefault(key, new ArrayList<>());
  }

  /**
   * Attempts to get a List of the given type
   * @param key Path to List in data structure.
   */
  default <T> List<T> getListParameterized(final String key) {
    return getOrSetDefault(key, new ArrayList<>());
  }

  default List<String> getStringList(final String key) {
    return getOrDefault(key, new ArrayList<>());
  }

  default List<Integer> getIntegerList(final String key) {
    return getOrDefault(key, new ArrayList<String>()).stream().map(Integer::parseInt).collect(Collectors.toList());
  }

  default List<Byte> getByteList(final String key) {
    return getOrDefault(key, new ArrayList<String>()).stream().map(Byte::parseByte).collect(Collectors.toList());
  }

  default List<Long> getLongList(final String key) {
    return getOrDefault(key, new ArrayList<String>()).stream().map(Long::parseLong).collect(Collectors.toList());
  }

  default Map<?, ?> getMap(final String key) {
    return getOrDefault(key, new HashMap<>());
  }

  /**
   * Attempts to get a map of the given type
   * @param key Path to the Map in the data-structure
   */
  default <K, V> Map<K, V> getMapParameterized(final String key) {
    return getOrSetDefault(key, new HashMap<>());
  }

  /**
   * Serialize an Enum from entry in the data-structure
   *
   * @param key      Path to Enum
   * @param enumType Class of the Enum
   * @param <E>      EnumType
   * @return Serialized Enum
   */
  default <E extends Enum<E>> E getEnum(
      final String key,
      final Class<E> enumType) {
    final Object object = get(key);
    Valid.checkBoolean(
        object instanceof String,
        "No usable Enum-Value found for '" + key + "'.");
    return Enum.valueOf(enumType, (String) object);
  }

  /**
   * Method to serialize a Class using the {@link SimplixSerializer}. You will need to register
   * your serializable in the {@link SimplixSerializer} before.
   *
   * @return Serialized instance of class.
   */
  @Nullable
  default <T> T getSerializable(final String key, final Class<T> clazz) {
    if (!contains(key)) {
      return null;
    }
    Object raw = get(key);
    if (raw == null) {
      return null;
    }
    return SimplixSerializer.deserialize(raw, clazz);
  }

  @Nullable
  default <T> List<T> getSerializableList(final String key, final Class<T> type) {
    if (!contains(key)) {
      return null;
    }

    final List<?> rawList = getList(key);

    return rawList
        .stream()
        .map(input -> SimplixSerializer.deserialize(input, type))
        .collect(Collectors.toList());
  }

  // ----------------------------------------------------------------------------------------------------
  // Advanced methods to save time.
  // ----------------------------------------------------------------------------------------------------

  /**
   * Returns the value for key in the data-structure, if it exists, else the specified default value.
   *
   * @param key Key to data in our data-structure.
   * @param def Default value, if data-structure doesn't contain key.
   * @param <T> Type of default-value.
   */
  default <T> T getOrDefault(final String key, @NonNull final T def) {
    final Object raw = get(key);
    return raw == null ? def : ClassWrapper.getFromDef(raw, def);
  }

  /**
   * Sets a value in the data-structure if the key doesn't yet exist.
   * Has nothing to do with Bukkit's 'addDefault' method.
   *
   * @param key   Key of data to set in our data-structure.
   * @param value Value to set.
   */
  default void setDefault(final String key, final Object value) {
    if (!contains(key)) {
      set(key, value);
    }
  }

  /**
   * Mix of setDefault & getDefault.
   * <p>Gets the value of the key in the data structure, casted to the type of the specified default def.
   * If the key doesn't yet exist, it will be created in the data-structure, set to def and afterwards returned.</p>
   *
   * @param key Key to set the value
   * @param def Value to set or return.
   */
  default <T> T getOrSetDefault(final String key, final T def) {
    final Object raw = get(key);
    //Key is not yet present in data-structure
    if (raw == null) {
      set(key, def);
      return def;
    } else {
      return ClassWrapper.getFromDef(raw, def);
    }
  }
}
