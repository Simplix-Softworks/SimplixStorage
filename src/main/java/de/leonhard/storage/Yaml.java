package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.util.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Yaml extends StorageCreator implements StorageBase {

    private File file;
    private YamlObject yamlObject;
    private String pathPrefix;
    private ReloadSettings reloadSettings;


    /*
    Inheritance
     */

    /*
    Structure:
    -Constructors:
    -Setters
    -Getters
    -private Methods (Reloaders etc.)
    -



     */

    public Yaml(String name, String path) {

        try {
            create(path, name, FileType.YAML);
            this.file = super.file;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        this.reloadSettings = ReloadSettings.intelligent;
//        System.out.println("UPDATING BECAUSE OF instanzing");
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings) {

        try {
            create(path, name, FileType.YAML);
            this.file = super.file;
        } catch (
                final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        update();
    }

    Yaml(final File file) {
        this.file = file;
        load(file);

        this.reloadSettings = ReloadSettings.intelligent;

        update();
    }


    @Override
    public void set(String key, Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {
            YamlObject old = yamlObject;
            yamlObject.put(finalKey, value);

            if (old.toString().equals(yamlObject.toString()))
                return;

            try {
                YamlWriter writer = new YamlWriter(new FileWriter(file));
                writer.write(yamlObject.toHashMap());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                old = null;
            }
        }
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


    @Override
    public <T> T getOrSetDefault(final String path, T def) {
        reload();
        if (!contains(path)) {
            set(path, def);
            return def;
        } else {
            return (T) getNotNested(path);
        }
    }


    @Override
    public Object get(final String key) {
        reload();
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return yamlObject.get(finalKey);
    }

    @Override
    public String getString(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        if (!contains(key))
            return "";

        return yamlObject.getString(finalKey);
    }

    @Override
    public long getLong(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getLong(finalKey);
    }

    @Override
    public int getInt(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getInt(finalKey);
    }

    @Override
    public byte getByte(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getByte(finalKey);
    }

    @Override
    public boolean getBoolean(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key)) {
            return false;

        }

        return yamlObject.getBoolean(finalKey);
    }


    @Override
    public boolean contains(String key) {

        key = (pathPrefix == null) ? key : pathPrefix + "." + key;

        return has(key);
    }

    private boolean has(String key) {
        reload();

        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Map map = (Map) getNotNested(parts[0]);

            return yamlObject.toHashMap().containsKey(parts[0]) && map.containsKey(parts[1]);
        }

        return yamlObject.toHashMap().containsKey(key);
    }

    @Override
    public float getFloat(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getFloat(finalKey);
    }

    @Override
    public double getDouble(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getDouble(finalKey);
    }


    @Override
    public List<?> getList(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        if (!contains(key))
            return new ArrayList<>();

        if (yamlObject.get(finalKey) instanceof String)
            return new ArrayList<>(Arrays.asList(((String) yamlObject.get(finalKey)).split("-")));


        return (List) yamlObject.get(key);
    }

    @Override
    public List<String> getStringList(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return new ArrayList<>();

        return (List<String>) yamlObject.get(finalKey);

    }

    @Override
    public List<Integer> getIntegerList(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return new ArrayList<>();

        return (List<Integer>) yamlObject.get(finalKey);

    }

    @Override
    public List<Byte> getByteList(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return new ArrayList<>();

        return (List<Byte>) yamlObject.get(finalKey);
    }

    @Override
    public List<Long> getLongList(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return new ArrayList<>();

        return (List<Long>) yamlObject.get(finalKey);
    }


    @Override
    public Map getMap(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return new HashMap();

        return (Map) yamlObject.get(finalKey);
    }


    private void reload() {

        if (reloadSettings.equals(ReloadSettings.manually))
            return;

        if (reloadSettings.equals(ReloadSettings.intelligent))
            if (!FileUtils.hasChanged(file, lastModified))
                return;

        update();
    }

    @Override
    public void update() {
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader(file));
            yamlObject = new YamlObject(reader.read());
        } catch (IOException e) {
            System.err.println("Exception while reloading yaml");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.err.println("Exception while closing file");
                e.printStackTrace();
            }
        }
//        System.out.println("UPDATED");
        this.lastModified = System.currentTimeMillis();
    }

    public String getPathPrefix() {
        return pathPrefix;
    }


    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
        reload();
    }

    private Object getNotNested(String key) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            HashMap result = (HashMap) getNotNested(parts[0]);
            return result.containsKey(parts[1]) ? result.get(parts[1]) : null;
        }
        return yamlObject.toHashMap().containsKey(key) ? yamlObject.toHashMap().get(key) : null;
    }

    public ReloadSettings getReloadSettings() {
        return reloadSettings;
    }

    public void setReloadSettings(ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
    }
}

