package de.leonhard.storage;

import java.util.List;
import java.util.Map;

public interface StorageBase {

    Object get(String key);

    /**
     * Get a String from a file
     *
     * @param key Path to String in file
     * @return Returns the value
     */

    String getString(final String key);

    /**
     * Gets a long from a file by key
     *
     * @param key Path to long in file
     * @return String from file
     */

    long getLong(final String key);

    /**
     * Gets a int from a file
     *
     * @param key Path to int in file
     * @return Int from file
     */

    int getInt(final String key);

    /**
     * Get a byte from a file
     *
     * @param key Path to byte in file
     * @return Byte from file
     */

    byte getByte(final String key);

    /**
     * Get a boolean from a file
     *
     * @param key Path to boolean in file
     * @return Boolean from file
     */

    boolean getBoolean(final String key);

    /**
     * Get a float from a file
     *
     * @param key Path to float in file
     * @return Float from file
     */

    float getFloat(final String key);

    /**
     * Get a double from a file
     *
     * @param key Path to double in file
     * @return Double from file
     */

    double getDouble(final String key);

    /**
     * Get a List from a file
     *
     * @param key Path to StringList in file
     * @return List
     */

    List<?> getList(final String key);

    /**
     * Get String List
     *
     * @param key Path to String List in file
     * @return List
     */

    List<String> getStringList(final String key);

    /**
     * Get a IntegerList from a file
     *
     * @param key Path to Integer-List in file
     * @return Integer-List
     */


    List<Integer> getIntegerList(final String key);

    /**
     * Get a Byte-List from a file
     *
     * @param key Path to Byte-List from file
     * @return Byte-List
     */

    List<Byte> getByteList(final String key);

    /**
     * Get a Long-List from a file
     *
     * @param key Path to Long-List in file
     * @return Long-List
     */

    List<Long> getLongList(final String key);

    /**
     * Gets a Map
     *
     * @param key Path to Map-List in file
     * @return Map
     */


    public Map getMap(final String key);


    /**
     * Returns the FilePath as String
     *
     * @return FilePath as String
     */
    String getFilePath();


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

    /**
     * Sets a value to the file if the file doesn't already contain the value (Not mix up with Bukkit addDefault)
     *
     * @param key   Key to set the value
     * @param value Value to set
     */

    void setDefault(String key, Object value);

    <T> T getOrSetDefault(String path, T def);


    void update();


}
