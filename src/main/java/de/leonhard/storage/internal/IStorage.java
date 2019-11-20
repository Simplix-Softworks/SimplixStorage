package de.leonhard.storage.internal;

import de.leonhard.storage.utils.ClassWrapper;
import de.leonhard.storage.utils.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public interface IStorage {

	Set<String> singleLayerKeySet();

	Set<String> singleLayerKeySet(String key);

	Set<String> keySet();

	Set<String> keySet(String key);

	void remove(String key);

	/**
	 * Set an object to your data-structure
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your data-structure
	 */
	void set(String key, Object value);

	/**
	 * Checks wheter a key exists in the data-structure
	 *
	 * @param key Key to check
	 * @return Returned value
	 */
	boolean contains(String key);

	Object get(String key);

	/**
	 * Get a value or a default one
	 *
	 * @param key Path to value in data-structure
	 * @param def Default value & type of it
	 */
	default <T> T get(String key, T def) {
		return contains(key) ? ClassWrapper.getFromDef(get(key), def) : def;
	}

	/**
	 * Get a String from a data-structure
	 *
	 * @param key Path to String in data-structure
	 * @return Returns the value
	 */
	default String getString(String key) {
		return contains(key) ? ClassWrapper.STRING.getString(get(key)) : "";
	}

	/**
	 * Gets a long from a data-structure by key
	 *
	 * @param key Path to long in data-structure
	 * @return String from data-structure
	 */
	default long getLong(String key) {
		return contains(key) ? ClassWrapper.LONG.getLong(get(key)) : 0L;
	}

	/**
	 * Gets an int from a data-structure
	 *
	 * @param key Path to int in data-structure
	 * @return Int from data-structure
	 */
	default int getInt(String key) {
		return contains(key) ? ClassWrapper.INTEGER.getInt(key) : 0;
	}

	/**
	 * Get a byte from a data-structure
	 *
	 * @param key Path to byte in data-structure
	 * @return Byte from data-structure
	 */
	default byte getByte(String key) {
		return contains(key) ? ClassWrapper.BYTE.getByte(get(key)) : 0;
	}

	/**
	 * Get a boolean from a data-structure
	 *
	 * @param key Path to boolean in data-structure
	 * @return Boolean from data-structure
	 */
	default boolean getBoolean(String key) {
		return contains(key) ? getString(key).equalsIgnoreCase("true") : false;
	}

	/**
	 * Get a float from a data-structure
	 *
	 * @param key Path to float in data-structure
	 * @return Float from data-structure
	 */
	default float getFloat(String key) {
		return contains(key) ? ClassWrapper.FLOAT.getFloat(get(key)) : 0F;
	}


	/**
	 * Get a double from a data-structure
	 *
	 * @param key Path to double in the data-structure
	 * @return Double from data-structure
	 */
	default double getDouble(String key) {
		return contains(key) ? ClassWrapper.DOUBLE.getDouble(get(key)) : 0D;
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
		Valid.checkBoolean(object instanceof String, "No Enum-Value found for key '" + key + "'.");
		return Enum.valueOf(enumType, (String) object);
	}

	/**
	 * Get a List from a data-structure
	 *
	 * @param key Path to StringList in data-structure
	 * @return List
	 */
	default List<?> getList(String key) {
		if (!contains(key)) {
			return new ArrayList<>();
		} else {
			return (List<?>) get(key);
		}
	}

	default List<String> getStringList(String key) {
		if (!contains(key)) {
			return new ArrayList<>();
		} else {
			return (List<String>) get(key);
		}
	}

	default List<Integer> getIntegerList(String key) {
		if (!contains(key)) {
			return new ArrayList<>();
		} else {
			return (List<Integer>) get(key);
		}
	}

	default List<Byte> getByteList(String key) {
		if (!contains(key)) {
			return new ArrayList<>();
		} else {
			return (List<Byte>) get(key);
		}
	}

	default List<Long> getLongList(String key) {
		if (!contains(key)) {
			return new ArrayList<>();
		} else {
			return (List<Long>) get(key);
		}
	}

	default Map getMap(String key) {
		return (Map) get(key);
	}

	/**
	 * Sets a value to the data-structure if the data-structure doesn't already contain the value
	 * (Don't mix up with Bukkit addDefault)
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */
	default void setDefault(String key, Object value) {
		if (!contains(key)) {
			set(key, value);
		}
	}

	default <T> T getOrSetDefault(String key, T def) {
		if (!contains(key)) {
			set(key, def);
			return def;
		} else {
			return ClassWrapper.getFromDef(get(key), def);
		}
	}
}