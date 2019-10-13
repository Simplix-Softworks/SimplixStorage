package de.leonhard.storage.base;

import de.leonhard.storage.util.Primitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "unchecked"})
public interface StorageBase {

    /**
     * Returns the FilePath as String
     *
     * @return FilePath as String
     */
    String getFilePath();

    void update();

    Set<String> keySet();

    Set<String> keySet(String key);


    Set<String> totalKeySet();

    Set<String> totalKeySet(String key);

    void remove(final String key);

    /**
     * Set a object to your file
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

    /**
     * Get a String from a file
     *
     * @param key Path to String in file
     * @return Returns the value
     */

    default String getString(final String key) {
        if (!contains(key))
            return "";
        return get(key).toString();
    }

    /**
     * Gets a long from a file by key
     *
     * @param key Path to long in file
     * @return String from file
     */

    default long getLong(final String key) {
        if (!contains(key))
            return 0L;
        return Primitive.LONG.getLong(get(key));
    }

    /**
     * Gets a int from a file
     *
     * @param key Path to int in file
     * @return Int from file
     */

    default int getInt(final String key) {
        if (!contains(key))
            return 0;
        return Primitive.INTEGER.getInt(get(key));
    }

    /**
     * Get a byte from a file
     *
     * @param key Path to byte in file
     * @return Byte from file
     */

    default byte getByte(final String key) {
        if (!contains(key))
            return 0;
        return Primitive.BYTE.getByte(get(key));
    }

    /**
     * Get a boolean from a file
     *
     * @param key Path to boolean in file
     * @return Boolean from file
     */

    default boolean getBoolean(final String key) {
        if (!contains(key))
            return false;
        return get(key).toString().equalsIgnoreCase("true");
    }

    /**
     * Get a float from a file
     *
     * @param key Path to float in file
     * @return Float from file
     */

    default float getFloat(final String key) {
        if (!contains(key))
            return 0F;
        return Primitive.FLOAT.getFloat(get(key));
    }

    /**
     * Get a double from a file
     *
     * @param key Path to double in file
     * @return Double from file
     */

    default double getDouble(final String key) {
        if (!contains(key))
            return 0D;
        return Primitive.DOUBLE.getDouble(get(key));
    }

    /**
     * Get a List from a file
     *
     * @param key Path to StringList in file
     * @return List
     */

    default List<?> getList(final String key) {
        if (!contains(key))
            return new ArrayList<>();
        return (List<?>) get(key);
    }

    /**
     * Get String List
     *
     * @param key Path to String List in file
     * @return List
     */

    default List<String> getStringList(final String key) {
        if (!contains(key))
            return new ArrayList<>();
        return (List<String>) get(key);
    }

    /**
     * Get a IntegerList from a file
     *
     * @param key Path to Integer-List in file
     * @return Integer-List
     */

    default List<Integer> getIntegerList(final String key) {
        if (!contains(key))
            return new ArrayList<>();
        return (List<Integer>) get(key);
    }

    /**
     * Get a Byte-List from a file
     *
     * @param key Path to Byte-List from file
     * @return Byte-List
     */

    default List<Byte> getByteList(final String key) {
        if (!contains(key))
            return new ArrayList<>();
        return (List<Byte>) get(key);
    }

    /**
     * Get a Long-List from a file
     *
     * @param key Path to Long-List in file
     * @return Long-List
     */

    default List<Long> getLongList(final String key) {
        if (!contains(key))
            return new ArrayList<>();
        return (List<Long>) get(key);
    }

    /**
     * Gets a Map
     *
     * @param key Path to Map-List in file
     * @return Map
     */

    default Map getMap(final String key) {
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
        if (contains(key))
            return;
        set(key, value);
    }

    default <T> T getOrSetDefault(String path, T def) {
        if (!contains(path)) {
            set(path, def);
            return def;
        } else {
            Object obj = get(path); //
            if (obj instanceof String && def instanceof Integer)
                obj = Integer.parseInt((String) obj);
            else if (obj instanceof String && def instanceof Double)
                obj = Double.parseDouble((String) obj);
            else if (obj instanceof String && def instanceof Float)
                obj = Double.parseDouble((String) obj);
            else if (obj instanceof String && def instanceof Boolean)
                return (T) (Boolean) obj.equals("true"); // Mustn't be primitive
            return (T) obj;
        }
    }
}