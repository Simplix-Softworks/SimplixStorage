package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.utils.basic.Primitive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	 * @param key Path to boolean in file
	 * @return Boolean from file
	 */
	default boolean getBoolean(@NotNull final String key) {
		if (!hasKey(key)) {
			return false;
		} else {
			return Primitive.Boolean.getBoolean(get(key));
		}
	}

	/**
	 * Checks whether a key exists in the file
	 *
	 * @param key Key to check
	 * @return Returned value
	 */
	boolean hasKey(@NotNull final String key);

	Object get(@NotNull final String key);

	/**
	 * Get a byte from a file
	 *
	 * @param key Path to byte in file
	 * @return Byte from file
	 */
	default byte getByte(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0;
		} else {
			return Primitive.BYTE.getByte(get(key));
		}
	}

	/**
	 * Get a Byte-List from a file
	 *
	 * @param key Path to Byte-List from file
	 * @return Byte-List
	 */
	default List<Byte> getByteList(@NotNull final String key) {
		if (!hasKey(key)) {
			return new ArrayList<>();
		} else {
			return (List<Byte>) get(key);
		}
	}

	/**
	 * Get a double from a file
	 *
	 * @param key Path to double in the file
	 * @return Double from file
	 */
	default double getDouble(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0D;
		} else {
			return Primitive.DOUBLE.getDouble(get(key));
		}
	}

	/**
	 * Get a float from a file
	 *
	 * @param key Path to float in file
	 * @return Float from file
	 */
	default float getFloat(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0F;
		} else {
			return Primitive.FLOAT.getFloat(get(key));
		}
	}

	/**
	 * Gets an int from a file
	 *
	 * @param key Path to int in file
	 * @return Int from file
	 */
	default int getInt(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0;
		} else {
			return Primitive.INTEGER.getInt(get(key));
		}
	}

	/**
	 * Gets a short from a file
	 *
	 * @param key Path to int in file
	 * @return Short from file
	 */
	default short getShort(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0;
		} else {
			return Primitive.SHORT.getShort(get(key));
		}
	}

	/**
	 * Get a IntegerList from a file
	 *
	 * @param key Path to Integer-List in file
	 * @return Integer-List
	 */
	default List<Integer> getIntegerList(@NotNull final String key) {
		if (!hasKey(key)) {
			return new ArrayList<>();
		} else {
			return (List<Integer>) get(key);
		}
	}

	/**
	 * Get a List from a file
	 *
	 * @param key Path to StringList in file
	 * @return List
	 */
	default List<?> getList(@NotNull final String key) {
		if (!hasKey(key)) {
			return new ArrayList<>();
		} else {
			return (List<?>) get(key);
		}
	}

	/**
	 * Gets a long from a file by key
	 *
	 * @param key Path to long in file
	 * @return String from file
	 */
	default long getLong(@NotNull final String key) {
		if (!hasKey(key)) {
			return 0L;
		} else {
			return Primitive.LONG.getLong(get(key));
		}
	}

	/**
	 * Get a Long-List from a file
	 *
	 * @param key Path to Long-List in file
	 * @return Long-List
	 */
	default List<Long> getLongList(@NotNull final String key) {
		if (!hasKey(key)) {
			return new ArrayList<>();
		} else {
			return (List<Long>) get(key);
		}
	}

	/**
	 * Gets a Map
	 *
	 * @param key Path to Map-List in file
	 * @return Map
	 */
	default Map getMap(@NotNull final String key) {
		if (!hasKey(key)) {
			return new HashMap();
		} else {
			return (Map) get(key);
		}
	}

	default <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		if (!hasKey(key)) {
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
			} else if (tempObj instanceof String && value instanceof Primitive.Boolean) {
				tempObj = ((String) tempObj).equalsIgnoreCase("true");
			}
			return (T) tempObj;
		}
	}

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	void set(@NotNull final String key, @Nullable final Object value);

	/**
	 * Get a String from a file
	 *
	 * @param key Path to String in file
	 * @return Returns the value
	 */
	default String getString(@NotNull final String key) {
		if (!hasKey(key)) {
			return "";
		} else {
			Object tempObject = get(key);
			return tempObject instanceof String ? (String) tempObject : tempObject.toString();
		}
	}

	/**
	 * Get String List
	 *
	 * @param key Path to String List in file
	 * @return List
	 */
	default List<String> getStringList(@NotNull final String key) {
		if (!hasKey(key)) {
			return new ArrayList<>();
		} else {
			return (List<String>) get(key);
		}
	}

	void remove(@NotNull final String key);

	/**
	 * Sets a value to the file if the file doesn't already contain the value
	 * (Not mix up with Bukkit addDefault)
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */
	default void setDefault(@NotNull final String key, @Nullable final Object value) {
		if (!hasKey(key)) {
			set(key, value);
		}
	}
}