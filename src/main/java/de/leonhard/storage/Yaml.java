package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.base.YamlBase;
import de.leonhard.storage.objects.YamlObject;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;

@Getter
@Setter
public class Yaml extends StorageCreator implements YamlBase {

    protected File file;
    @Setter(AccessLevel.PRIVATE)
    protected YamlObject yamlObject;
    protected String pathPrefix;
    protected ReloadSettings reloadSettings;
    protected ConfigSettings configSettings;
    protected final YamlEditor yamlEditor;
    protected final YamlParser parser;


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
        this.configSettings = ConfigSettings.skipComments;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings) {

        try {
            create(path, name, FileType.YAML);
            this.file = super.file;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        this.configSettings = ConfigSettings.skipComments;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        update();
    }

    Yaml(final File file) {
        this.file = file;
        load(file);

        update();

        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        this.reloadSettings = ReloadSettings.intelligent;
        this.configSettings = ConfigSettings.skipComments;
    }

    @Override
    public void set(String key, Object value) {
        insert(key, value);
    }


    @Override
    public void set(String key, Object value, ConfigSettings configSettings) {
        insert(key, value);
    }


    private void insert(String key, Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = yamlObject.toString();
            yamlObject.put(finalKey, value);

            if (old.equals(yamlObject.toString()) && yamlObject != null)
                return;

            try {
                if (configSettings.equals(ConfigSettings.preserveComments)) {

                    final List<String> unEdited = yamlEditor.read();
                    final List<String> header = yamlEditor.readHeader();
                    final List<String> footer = yamlEditor.readFooter();
                    write(yamlObject.toHashMap());
                    final List<String> lines = header;
                    lines.addAll(yamlEditor.read());
                    lines.addAll(footer);

                    yamlEditor.write(parser.parseComments(unEdited, lines));
                    return;
                }
                write(yamlObject.toHashMap());

            } catch (final IOException e) {
                System.err.println("Error while writing '" + file.getName() + "'");
            }
            old = null;
        }
    }

    @Override
    public void write(Map data) throws IOException {
        YamlWriter writer = new YamlWriter(new FileWriter(file));
        writer.write(data);
        writer.close();
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
            Object obj = get(path); //
            if (obj instanceof String && def instanceof Integer)
                obj = Integer.parseInt((String) obj);
            if (obj instanceof String && def instanceof Double)
                obj = Double.parseDouble((String) obj);
            if (obj instanceof String && def instanceof Float)
                obj = Double.parseDouble((String) obj);
            return (T) obj;
        }
    }

//    @Override
//    public void remove(String key) {
//        String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
//        try {
//            write(Utils.remove(yamlObject.toHashMap(), finalKey));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public Object get(final String key) {
        reload();
        String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
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
            return Utils.contains(key, yamlObject.toHashMap());
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
            if (FileUtils.hasNotChanged(file, lastModified))
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

    @Override
    public List<String> getHeader() {
        try {
            return yamlEditor.readHeader();
        } catch (IOException e) {
            System.err.println("Error while getting header of '" + file.getName() + "'");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void removeKey(final String key) {
        final Map obj = yamlObject.toHashMap();
        obj.remove(key);

        yamlObject = new YamlObject(obj);
    }

    @Override
    public Set<String> getKeySet() {
        reload();
        return yamlObject.toHashMap().keySet();
    }

    public void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        if (!finalKey.contains("."))
            removeKey(key);
        final Map<String, Object> old = yamlObject.toHashMap();
        yamlObject = new YamlObject(Utils.remove(old, finalKey));
        try {
            write(yamlObject.toHashMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConfigSettings(final ConfigSettings configSettings) {
        this.configSettings = configSettings;
    }
}

