package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.utils.basic.Primitive;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic Interface for the Data Classes
 */
@SuppressWarnings({"unused", "unchecked"})
public interface StorageBase {

	/**
	 * Get a boolean from a file
	 *
	 * @param key Key to boolean in file
	 * @return Boolean from file
	 */
	default boolean getBoolean(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.BOOLEAN.getBoolean(get(key));
		}
	}

	Object get(final @NotNull String key);

	default Map<String, Object> getAll(final @NotNull String... keys) {
		return getAll(Arrays.asList(keys));
	}

	Map<String, Object> getAll(final @NotNull List<String> keys);

	default Map<String, Object> getAll(final @NotNull String key, final @NotNull String... keys) {
		return getAll(key, Arrays.asList(keys));
	}

	Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys);

	/**
	 * Get a byte from a file
	 *
	 * @param key Key to byte in file
	 * @return Byte from file
	 */
	default byte getByte(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.BYTE.getByte(get(key));
		}
	}

	/**
	 * Get a Byte-List from a file
	 *
	 * @param key Key to Byte-List from file
	 * @return Byte-List
	 */
	default List<Byte> getByteList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return (List<Byte>) tempObject;
		}
	}

	/**
	 * Get a double from a file
	 *
	 * @param key Key to double in the file
	 * @return Double from file
	 */
	default double getDouble(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.DOUBLE.getDouble(get(key));
		}
	}

	/**
	 * Get a float from a file
	 *
	 * @param key Key to float in file
	 * @return Float from file
	 */
	default float getFloat(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.FLOAT.getFloat(get(key));
		}
	}

	/**
	 * Gets an int from a file
	 *
	 * @param key Key to int in file
	 * @return Int from file
	 */
	default int getInt(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.INTEGER.getInt(get(key));
		}
	}

	/**
	 * Gets a short from a file
	 *
	 * @param key Key to int in file
	 * @return Short from file
	 */
	default short getShort(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.SHORT.getShort(get(key));
		}
	}

	/**
	 * Get a IntegerList from a file
	 *
	 * @param key Key to Integer-List in file
	 * @return Integer-List
	 */
	default List<Integer> getIntegerList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return (List<Integer>) tempObject;
		}
	}

	/**
	 * Get a List from a file
	 *
	 * @param key Key to StringList in file
	 * @return List
	 */
	default List getList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return (List) tempObject;
		}
	}

	/**
	 * Gets a long from a File by key
	 *
	 * @param key Key to long in file
	 * @return String from file
	 */
	default long getLong(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return Primitive.LONG.getLong(get(key));
		}
	}

	/**
	 * Get a Long-List from a file
	 *
	 * @param key Key to Long-List in file
	 * @return Long-List
	 */
	default List<Long> getLongList(final @NotNull String key) {
		Object tempObject = this.get(key);
		return tempObject != null ? (List<Long>) get(key) : null;
	}

	/**
	 * Gets a Map
	 *
	 * @param key Key to Map-List in file
	 * @return Map
	 */
	default Map getMap(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return (Map) tempObject;
		}
	}

	/**
	 * Sets a value to the File if the File doesn't already contain the value or returns the value if the value exists
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 * @return the value set in the File
	 */
	default <T> T getOrSetDefault(final @NotNull String key, final @NotNull T value) {
		if (!this.hasKey(key)) {
			set(key, value);
			return value;
		} else {
			Object tempObj = get(key);
			if (tempObj instanceof String && value instanceof Integer) {
				tempObj = Integer.parseInt((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Long) {
				tempObj = Long.parseLong((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Double) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Float) {
				tempObj = Double.parseDouble((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Short) {
				tempObj = Short.parseShort((String) tempObj);
			} else if (tempObj instanceof String && value instanceof Primitive.BOOLEAN) {
				tempObj = ((String) tempObj).equalsIgnoreCase("true");
			}
			return (T) tempObj;
		}
	}

	/**
	 * Checks whether a key exists in the file
	 *
	 * @param key Key to check
	 * @return true if key exists
	 */
	boolean hasKey(final @NotNull String key);

	/**
	 * Set an Object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	void set(final @NotNull String key, final @Nullable Object value);

	void setAll(final @NotNull Map<String, Object> dataMap);

	void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap);

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	Set<String> keySet();

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the keySet of all sublayers of the given key or an empty set if the key does not exist (Format: key.subkey).
	 */
	Set<String> keySet(final @NotNull String key);

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	Set<String> singleLayerKeySet();

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or an empty set if the key does not exist.
	 */
	Set<String> singleLayerKeySet(final @NotNull String key);

	/**
	 * Get an Object from the File casted to a certain type
	 *
	 * @param key Key to value in file
	 * @param def the Class to be casted to
	 * @param <T> returnType
	 * @return returns the value of the key casted to def
	 */
	default <T> T get(final @NotNull String key, final @NotNull Class def) {
		return Primitive.getFromDef(getString(key), def);
	}

	/**
	 * Get a String from a file
	 *
	 * @param key Key to String in file
	 * @return Returns the value
	 */
	default String getString(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return tempObject instanceof String ? (String) tempObject : tempObject.toString();
		}
	}

	/**
	 * Get String List
	 *
	 * @param key Key to String List in file
	 * @return List
	 */
	default List<String> getStringList(final @NotNull String key) {
		Object tempObject = this.get(key);
		if (tempObject == null) {
			throw new IllegalStateException("Key '" + key + "' does not exist");
		} else {
			return (List<String>) tempObject;
		}
	}

	void remove(final @NotNull String key);

	default void removeAll(final @NotNull String... keys) {
		removeAll(Arrays.asList(keys));
	}

	void removeAll(final @NotNull List<String> keys);

	default void removeAll(final @NotNull String key, final @NotNull String... keys) {
		removeAll(key, Arrays.asList(keys));
	}

	void removeAll(final @NotNull String key, final @NotNull List<String> keys);

	/**
	 * Sets a value to the File if the File doesn't already contain the value
	 * (Do not mix up with Bukkit addDefault)
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */
	default void setDefault(final @NotNull String key, final @Nullable Object value) {
		if (!this.hasKey(key)) {
			set(key, value);
		}
	}

	FlatSection getSection(final @NotNull String sectionKey);
}