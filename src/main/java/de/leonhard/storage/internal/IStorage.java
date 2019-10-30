package de.leonhard.storage.internal;

import de.leonhard.storage.utils.Primitive;
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
	 * @param key Path to value in data-structure
	 * @param def Default value & type of it
	 */
	default <T> T get(String key, T def) {
		if (!contains(key)) {
			return def;
		}
		return Primitive.getFromDef(get(key), def);
	}

	/**
	 * Get a String from a data-structure
	 *
	 * @param key Path to String in data-structure
	 * @return Returns the value
	 */
	default String getString(String key) {
		if (!contains(key)) {
			return "";
		} else {
			return get(key).toString();
		}
	}

	/**
	 * Gets a long from a data-structure by key
	 *
	 * @param key Path to long in data-structure
	 * @return String from data-structure
	 */
	default long getLong(String key) {
		if (!contains(key)) {
			return 0L;
		} else {
			return Primitive.LONG.getLong(get(key));
		}
	}

	/**
	 * Gets an int from a data-structure
	 *
	 * @param key Path to int in data-structure
	 * @return Int from data-structure
	 */
	default int getInt(String key) {
		if (!contains(key)) {
			return 0;
		} else {
			return Primitive.INTEGER.getInt(get(key));
		}
	}

	/**
	 * Get a byte from a data-structure
	 *
	 * @param key Path to byte in data-structure
	 * @return Byte from data-structure
	 */
	default byte getByte(String key) {
		if (!contains(key)) {
			return 0;
		} else {
			return Primitive.BYTE.getByte(get(key));
		}
	}

	/**
	 * Get a boolean from a data-structure
	 *
	 * @param key Path to boolean in data-structure
	 * @return Boolean from data-structure
	 */
	default boolean getBoolean(String key) {
		if (!contains(key)) {
			return false;
		} else {
			return get(key).toString().equalsIgnoreCase("true");
		}
	}

	/**
	 * Get a float from a data-structure
	 *
	 * @param key Path to float in data-structure
	 * @return Float from data-structure
	 */
	default float getFloat(String key) {
		if (!contains(key)) {
			return 0F;
		} else {
			return Primitive.FLOAT.getFloat(get(key));
		}
	}


	/**
	 * Get a double from a data-structure
	 *
	 * @param key Path to double in the data-structure
	 * @return Double from data-structure
	 */
	default double getDouble(String key) {
		if (!contains(key)) {
			return 0D;
		} else {
			return Primitive.DOUBLE.getDouble(get(key));
		}
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
	 * (Not mix up with Bukkit addDefault)
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
			return Primitive.getFromDef(get(key), def);
		}
	}
}