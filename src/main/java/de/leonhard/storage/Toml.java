package de.leonhard.storage;

import de.leonhard.storage.base.TomlBase;
import de.leonhard.storage.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Toml extends StorageCreator implements TomlBase {
    private Map<String, Object> data;
    private File file;
    private String pathPrefix;
    private final ReloadSettings reloadSettings;

    public Toml(final String name, final String path) {
        try {
            create(path, name, FileType.TOML);
            this.file = super.file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = ReloadSettings.intelligent;
        update();
    }

    public Toml(final String name, final String path, final ReloadSettings reloadSettings) {
        try {
            create(name, path, FileType.YAML);
            this.file = super.file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        update();
    }

    Toml(final File file) {

        this.file = file;
        this.reloadSettings = ReloadSettings.intelligent;
        update();
    }

    /**
     * Set a object to your file
     *
     * @param key   The key your value should be associated with
     * @param value The value you want to set in your file
     */
    @Override
    public void set(final String key, Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final String old = data.toString();

        if (finalKey.contains("."))
            data = Utils.stringToMap(finalKey, value, data);
        else
            data.put(finalKey, value);


        if (old.equals(data.toString()))
            return;

        try {
            com.electronwill.toml.Toml.write(data, file);
        } catch (IOException e) {
            System.err.println("Exception while writing to Toml file '" + file.getName() + "'");
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String key) {
        reload();
        return Utils.get(key, data);
    }

    /**
     * Get a String from a file
     *
     * @param key Path to String in file
     * @return Returns the value
     */
    @Override
    public String getString(String key) {
        return (contains(key)) ? (String) get(key) : "";
    }

    /**
     * Gets a long from a file by key
     *
     * @param key Path to long in file
     * @return String from file
     */
    @Override
    public long getLong(String key) {
        final Object obj = get(key);
        if (obj instanceof Integer)
            return (long) (int) obj;
        return (contains(key)) ? (long) obj : 0;

    }

    /**
     * Gets a int from a file
     *
     * @param key Path to int in file
     * @return Int from file
     */
    @Override
    public int getInt(String key) {
        return (contains(key)) ? (int) get(key) : 0;
    }

    /**
     * Get a byte from a file
     *
     * @param key Path to byte in file
     * @return Byte from file
     */
    @Override
    public byte getByte(String key) {
        return (contains(key)) ? (byte) get(key) : 0;
    }

    /**
     * Get a boolean from a file
     *
     * @param key Path to boolean in file
     * @return Boolean from file
     */
    @Override
    public boolean getBoolean(String key) {
        return (contains(key)) ? (boolean) get(key) : false;
    }

    /**
     * Get a float from a file
     *
     * @param key Path to float in file
     * @return Float from file
     */
    @Override
    public float getFloat(String key) {
        final Object obj = get(key);
        if (obj instanceof Integer)
            return (float) (int) obj;
        return (contains(key)) ? (float) obj : 0;
    }

    /**
     * Get a double from a file
     *
     * @param key Path to double in file
     * @return Double from file
     */
    @Override
    public double getDouble(String key) {
        final Object obj = get(key);
        if (obj instanceof Integer)
            return (double) (int) obj;
        return (contains(key)) ? (double) obj : 0;
    }

    /**
     * Get a List from a file
     *
     * @param key Path to StringList in file
     * @return List
     */
    @Override
    public List<?> getList(String key) {
        return (contains(key)) ? (List<?>) get(key) : new ArrayList<>();
    }

    /**
     * Get String List
     *
     * @param key Path to String List in file
     * @return List
     */
    @Override
    public List<String> getStringList(String key) {
        return (contains(key)) ? (List<String>) get(key) : new ArrayList<>();
    }

    /**
     * Get a IntegerList from a file
     *
     * @param key Path to Integer-List in file
     * @return Integer-List
     */
    @Override
    public List<Integer> getIntegerList(String key) {
        return (contains(key)) ? (List<Integer>) get(key) : new ArrayList<>();
    }

    /**
     * Get a Byte-List from a file
     *
     * @param key Path to Byte-List from file
     * @return Byte-List
     */
    @Override
    public List<Byte> getByteList(String key) {
        return (contains(key)) ? (List<Byte>) get(key) : new ArrayList<>();
    }

    /**
     * Get a Long-List from a file
     *
     * @param key Path to Long-List in file
     * @return Long-List
     */
    @Override
    public List<Long> getLongList(String key) {
        return (contains(key)) ? (List<Long>) get(key) : new ArrayList<>();
    }

    /**
     * Gets a Map
     *
     * @param key Path to Map-List in file
     * @return Map
     */
    @Override
    public Map getMap(String key) {
        return (contains(key)) ? (Map) get(key) : new HashMap();
    }

    /**
     * Returns the FilePath as String
     *
     * @return FilePath as String
     */
    @Override
    public String getFilePath() {
        return null;
    }

    /**
     * Checks wheter a key exists in the file
     *
     * @param key Key to check
     * @return Returned value
     */
    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return Utils.contains(finalKey, data);
    }

    /**
     * Sets a value to the file if the file doesn't already contain the value (Not mix up with Bukkit addDefault)
     *
     * @param key   Key to set the value
     * @param value Value to set
     */
    @Override
    public void setDefault(String key, Object value) {
        if (contains(key)) {
            return;
        }
        set(key, value);
    }

    @Override
    public <T> T getOrSetDefault(String path, T def) {
        reload();
        if (!contains(path)) {
            set(path, def);
            return def;
        } else {
            return (T) get(path);
        }
    }

    @Override
    public void update() {
        try {
            data = com.electronwill.toml.Toml.read(file);
        } catch (IOException e) {
            System.err.println("Exception while reading '" + file.getName() + "'");
            e.printStackTrace();
        }
    }

    /**
     * Reloads the file when needed see {@link ReloadSettings}
     * for deeper information
     */
    private void reload() {
        if (!shouldReload(reloadSettings))
            return;
        update();
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
