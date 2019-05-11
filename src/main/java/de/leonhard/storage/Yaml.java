package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Yaml extends StorageCreator implements StorageBase {
    @Override
    public File getFile() {
        return file;
    }

    private File file;
    private YamlObject yamlObject;

    //TODO Header

    public Yaml(String name, String path) {
        File newFile = new File(path + File.separator + name + ".yml");

        try {
            if (!newFile.exists()) {
                create(path, name, FileType.YAML);
                this.file = super.file;
                return;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.file = newFile;
    }

    /**
     * Sets a value to the yaml if the file doesn't already contain the value (Not mix up with Bukkit addDefault)
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


    private void reload() {
        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            yamlObject = new YamlObject(reader.read());
        } catch (IOException e) {
            System.err.println("Exception while reloading yaml");
            e.printStackTrace();
        }
    }

    /**
     * Get a String from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to String in YAML-File
     * @return Returns the value
     */
    @Override
    public String getString(String key) {
        reload();
        if (!contains(key))
            return null;
        return yamlObject.getString(key);
    }

    /**
     * Gets a long from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to long in YAML-FILE
     * @return long from YAML
     */
    @Override
    public long getLong(String key) {
        reload();

        if (!contains(key))
            return 0;

        return yamlObject.getLong(key);
    }


    /**
     * Gets a int from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to int in YAML-File
     * @return Int from YAML
     */
    @Override
    public int getInt(String key) {
        reload();

        if (!contains(key))
            return 0;

        return yamlObject.getInt(key);
    }

    /**
     * Get a byte from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to byte in YAML-File
     * @return Byte from YAML
     */
    @Override
    public byte getByte(String key) {
        reload();

        if (!contains(key))
            return 0;

        return yamlObject.getByte(key);
    }

    /**
     * Get a boolean from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to boolean in YAML-File
     * @return Boolean from YAML
     */
    @Override
    public boolean getBoolean(String key) {
        reload();

        if (!contains(key))
            return false;

        if (yamlObject.get(key) instanceof String) {
            return yamlObject.get(key).equals("true");
        }
        return (boolean) yamlObject.get(key);
    }

    /**
     * Get a float from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to float in YAML-File
     * @return Float from YAML
     */
    @Override
    public float getFloat(String key) {
        reload();

        if (!contains(key))
            return 0;

        return yamlObject.getFloat(key);
    }

    /**
     * Get a double from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to double in YAML-File
     * @return Double from YAML
     */
    @Override
    public double getDouble(String key) {
        reload();

        if (!contains(key))
            return 0;

        return yamlObject.getDouble(key);
    }

    /**
     * Get a List from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to List in YAML-File
     * @return List
     */
    @Override
    public List<?> getList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        if (yamlObject.get(key) instanceof String)
            return new ArrayList<>(Arrays.asList(((String) yamlObject.get(key)).split("-")));


        return (List) yamlObject.get(key);
    }

    /**
     * Get String List
     * Uses {@link YamlObject}
     *
     * @param key Path to String List in YAML-File
     * @return List
     */
    @Override
    public List<String> getStringList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<String>) yamlObject.get(key);//TODO converter? Check if converter is needed

    }

    /**
     * Get a IntegerList from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to Integer-List in YAML-File
     * @return Integer-List
     */
    @Override
    public List<Integer> getIntegerList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<Integer>) yamlObject.get(key);

    }

    /**
     * Get a Byte-List from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to Byte-List from YAML-File
     * @return Byte-List
     */
    @Override
    public List<Byte> getByteList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<Byte>) yamlObject.get(key);
    }

    /**
     * Get a Long-List from a YAML-File
     * Uses {@link YamlObject}
     *
     * @param key Path to Long-List to YAML-File
     * @return Long-List
     */
    @Override
    public List<Long> getLongList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<Long>) yamlObject.get(key);
    }


    /**
     * Gets a Map by key
     * Although used to get nested objects {@link Yaml}
     *
     * @param key Path to Map-List in JSON
     * @return Map
     */


    @Override//TODO Nested Maps
    public Map getMap(String key) {
        reload();
        return (Map) yamlObject.get(key);
    }

    @Override
    public String getFilePath() {
        return file.getAbsolutePath();
    }


    @Override
    public void set(String key, Object value) {
        YamlReader reader;
        synchronized (this) {
            try {
                reader = new YamlReader(new FileReader(file));
                yamlObject = new YamlObject(reader.read());
                yamlObject.put(key, value);
                YamlWriter writer = new YamlWriter(new FileWriter(file));
                writer.write(yamlObject.toHashMap());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean contains(String key) {
        reload();
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            return yamlObject.toHashMap().containsKey(parts[0]) && getMap(parts[0]).containsKey(parts[1]);
        }

        return yamlObject.toHashMap().containsKey(key);
    }


}

