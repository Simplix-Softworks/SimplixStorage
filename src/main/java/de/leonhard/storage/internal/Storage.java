package de.leonhard.storage.internal;

import de.leonhard.storage.internal.serialize.LightningSerializer;
import de.leonhard.storage.util.ClassWrapper;
import de.leonhard.storage.util.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Storage {

	Set<String> singleLayerKeySet();

	Set<String> singleLayerKeySet(String key);

	Set<String> keySet();

	Set<String> keySet(String key);

	void remove(String key);

	/**
	 * Set an object to your data-structure
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your data-structure.
	 */
	void set(String key, Object value);

	/**
	 * Method to deserialize a class using the {@link LightningSerializer}.
	 * You will need to register your serializable in the {@link LightningSerializer} before.
	 *
	 * @param key   The key your value should be associated with.
	 * @param value The value you want to set in your data-structure.
	 */
	default void setSerializable(String key, Object value) {
		final Object data = LightningSerializer.deserialize(value);
		set(key, data);
	}

	/**
	 * Checks whether a key exists in the data-structure
	 *
	 * @param key Key to check
	 * @return Returned value.
	 */
	boolean contains(String key);

	Object get(String key);

	// ----------------------------------------------------------------------------------------------------
	// Getting primitive types from data-structure
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Get a value or a default one
	 *
	 * @param key Path to value in data-structure
	 * @param def Default value & type of it
	 */
	default <T> T get(String key, T def) {
		if (!contains(key)) {
			return def;
		}
		return ClassWrapper.getFromDef(get(key), def);
	}

	/**
	 * Get a String from a data-structure
	 *
	 * @param key Path to String in data-structure
	 * @return Returns the value
	 */
	default String getString(String key) {
		return getOrDefault(key, "");
	}

	/**
	 * Gets a long from a data-structure by key
	 *
	 * @param key Path to long in data-structure
	 * @return String from data-structure
	 */
	default long getLong(String key) {
		return getOrDefault(key, 0L);
	}

	/**
	 * Gets an int from a data-structure
	 *
	 * @param key Path to int in data-structure
	 * @return Int from data-structure
	 */
	default int getInt(String key) {
		return getOrDefault(key, 0);
	}

	/**
	 * Get a byte from a data-structure
	 *
	 * @param key Path to byte in data-structure
	 * @return Byte from data-structure
	 */
	default byte getByte(String key) {
		return getOrDefault(key, (byte) 0);
	}

	/**
	 * Get a boolean from a data-structure
	 *
	 * @param key Path to boolean in data-structure
	 * @return Boolean from data-structure
	 */
	default boolean getBoolean(String key) {
		return getOrDefault(key, false);
	}

	/**
	 * Get a float from a data-structure
	 *
	 * @param key Path to float in data-structure
	 * @return Float from data-structure
	 */
	default float getFloat(String key) {
		return getOrDefault(key, 0F);
	}


	/**
	 * Get a double from a data-structure
	 *
	 * @param key Path to double in the data-structure
	 * @return Double from data-structure
	 */
	default double getDouble(String key) {
		return getOrDefault(key, 0D);
	}

	// ----------------------------------------------------------------------------------------------------
	// Getting Lists and non-ClassWrapper types from data-structure
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Get a List from a data-structure
	 *
	 * @param key Path to StringList in data-structure.
	 * @return List
	 */
	default List<?> getList(String key) {
		return getOrDefault(key, new ArrayList<>());
	}

	default List<String> getStringList(String key) {
		return getOrDefault(key, new ArrayList<>());
	}

	default List<Integer> getIntegerList(String key) {
		return getOrDefault(key, new ArrayList<>());
	}

	default List<Byte> getByteList(String key) {
		return getOrDefault(key, new ArrayList<>());
	}

	default List<Long> getLongList(String key) {
		return getOrDefault(key, new ArrayList<>());
	}

	default Map getMap(String key) {
		return (Map) get(key);
	}

	/**
	 * Serialize an Enum from entry in the data-structure
	 *
	 * @param key      Path to Enum
	 * @param enumType Class of the Enum
	 * @param <E>      EnumType
	 * @return Serialized Enum
	 */
	default <E extends Enum<E>> E getEnum(String key, Class<E> enumType) {
		Object object = get(key);
		Valid.checkBoolean(object instanceof String, "No usable Enum-Value found for '" + key + "'.");
		return Enum.valueOf(enumType, (String) object);
	}

	/**
	 * Method to serialize a Class using the {@link LightningSerializer}.
	 * You will need to register your serializable in the {@link LightningSerializer} before.
	 *
	 * @return Serialized instance of class.
	 */
	default <T> T getSerializable(String key, Class<T> clazz) {
		if (!contains(key)) {
			return null;

		}
		return LightningSerializer.serialize(get(key), clazz);
	}

	// ----------------------------------------------------------------------------------------------------
	// Advanced methods to save time.
	// ----------------------------------------------------------------------------------------------------

	/**
	 * @param key Key to data in our data-structure.
	 * @param def Default value, if data-structure doesn't contain key.
	 * @param <T> Type of default-value.
	 */
	default <T> T getOrDefault(String key, T def) {
		if (!contains(key)) {
			return def;
		}
		return ClassWrapper.getFromDef(get(key), def);
	}

	/**
	 * Sets a value to the data-structure if the data-structure doesn't already contain the value
	 * Has nothing to do with Bukkit't 'addDefault'
	 *
	 * @param key   Key to set the value
	 * @param value Value to set.
	 */
	default void setDefault(String key, Object value) {
		if (!contains(key)) {
			set(key, value);
		}
	}

	/**
	 * Mix of setDefault & getDefault.
	 * <p>
	 * Sets a value to the data-structure if the data-structure doesn't already contain the value
	 * Returns a default value if the data-structure doesn't already contain the key.
	 * <p>
	 * If the key is already contained by the data-structure the value of assigned to
	 * the key will be returned and casted to the type of your def.
	 *
	 * @param key Key to set the value
	 * @param def Value to set or return.
	 */
	default <T> T getOrSetDefault(String key, T def) {
		if (!contains(key)) {
			set(key, def);
			return def;
		} else {
			return ClassWrapper.getFromDef(get(key), def);
		}
	}
}