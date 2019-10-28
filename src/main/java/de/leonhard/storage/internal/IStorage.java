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
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	void set(String key, Object value);

	/**
	 * Checks wheter a key exists in the file
	 *
	 * @param key Key to check
	 * @return Returned value
	 */
	boolean contains(String key);

	Object get(String key);

	default <T> T get(String key, T def) {
		if (!contains(key)) {
			return def;
		}
		return Primitive.getFromDef(get(key), def);
	}

	/**
	 * Get a String from a file
	 *
	 * @param key Path to String in file
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
	 * Gets a long from a file by key
	 *
	 * @param key Path to long in file
	 * @return String from file
	 */
	default long getLong(String key) {
		if (!contains(key)) {
			return 0L;
		} else {
			return Primitive.LONG.getLong(get(key));
		}
	}

	/**
	 * Gets an int from a file
	 *
	 * @param key Path to int in file
	 * @return Int from file
	 */
	default int getInt(String key) {
		if (!contains(key)) {
			return 0;
		} else {
			return Primitive.INTEGER.getInt(get(key));
		}
	}

	/**
	 * Get a byte from a file
	 *
	 * @param key Path to byte in file
	 * @return Byte from file
	 */
	default byte getByte(String key) {
		if (!contains(key)) {
			return 0;
		} else {
			return Primitive.BYTE.getByte(get(key));
		}
	}

	/**
	 * Get a boolean from a file
	 *
	 * @param key Path to boolean in file
	 * @return Boolean from file
	 */
	default boolean getBoolean(String key) {
		if (!contains(key)) {
			return false;
		} else {
			return get(key).toString().equalsIgnoreCase("true");
		}
	}

	/**
	 * Get a float from a file
	 *
	 * @param key Path to float in file
	 * @return Float from file
	 */
	default float getFloat(String key) {
		if (!contains(key)) {
			return 0F;
		} else {
			return Primitive.FLOAT.getFloat(get(key));
		}
	}


	/**
	 * Get a double from a file
	 *
	 * @param key Path to double in the file
	 * @return Double from file
	 */
	default double getDouble(String key) {
		if (!contains(key)) {
			return 0D;
		} else {
			return Primitive.DOUBLE.getDouble(get(key));
		}
	}

	/**
	 * Serialize an Enum from entry in the file
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
	 * Get a List from a file
	 *
	 * @param key Path to StringList in file
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
	 * Sets a value to the file if the file doesn't already contain the value
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