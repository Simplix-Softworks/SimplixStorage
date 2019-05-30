package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.base.YamlBase;
import de.leonhard.storage.util.FileUtils;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.DumperOptions;

import java.io.*;
import java.util.*;

@Getter
@Setter
public class Yaml extends StorageCreator implements YamlBase {

    private File file;
    private YamlObject yamlObject;
    private String pathPrefix;
    private ReloadSettings reloadSettings;
    private org.yaml.snakeyaml.Yaml yaml;
    private DumperOptions dumperOptions;


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
        this.yaml = new org.yaml.snakeyaml.Yaml();
        yaml.setSkipComments(false);
        this.dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
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
        this.yaml = new org.yaml.snakeyaml.Yaml();
        yaml.setSkipComments(false);
        this.dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);

        update();
    }

    Yaml(final File file) {
        this.file = file;
        load(file);

        this.reloadSettings = ReloadSettings.intelligent;
        this.yaml = new org.yaml.snakeyaml.Yaml();
        yaml.setSkipComments(false);
        this.dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);

        update();
    }

    @Override
    public void set(String key, Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = yamlObject.toString();
            yamlObject.put(finalKey, value);

            if (old.equals(yamlObject.toString()) && yamlObject != null)
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
    public byte getByte(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;


        if (!contains(key))
            return 0;

        return yamlObject.getByte(finalKey);
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
    public long getLong(String key) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        if (!contains(key))
            return 0;

        return yamlObject.getLong(finalKey);
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
        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            yamlObject = new YamlObject(reader.read());
        } catch (IOException e) {
            System.err.println("Exception while reloading yaml");
            e.printStackTrace();
        }
        this.lastModified = System.currentTimeMillis();
    }

    private Object getNotNested(String key) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            HashMap result = (HashMap) getNotNested(parts[0]);
            return result.containsKey(parts[1]) ? result.get(parts[1]) : null;
        }
        return yamlObject.toHashMap().containsKey(key) ? yamlObject.toHashMap().get(key) : null;
    }


    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
        reload();
    }
}

