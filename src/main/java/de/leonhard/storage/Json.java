package de.leonhard.storage;

import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.HashMapUtil;
import de.leonhard.storage.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json extends StorageCreator implements StorageBase {
    private JSONObject object;
    private File file;
    private String pathPrefix;
    private ReloadSettings reloadSettings;

    /**
     * Creates a .json file where you can put your data in.+
     *
     * @param name Name of the .json file
     * @param path Absolute path, where the .json file should be created.
     */

    public Json(final String name, final String path) {

        try {
            create(path, name, FileType.JSON);

            this.file = super.file;


            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException |
                    NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(2));
                writer.close();
            }

            JSONTokener tokener = new JSONTokener(fis);
            object = new JSONObject(tokener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
            ex.printStackTrace();
        }

        this.reloadSettings = ReloadSettings.intelligent;
    }

    public Json(final String name, final String path, ReloadSettings reloadSettings) {

        try {
            create(path, name, FileType.JSON);

            this.file = super.file;


            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException |
                    NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(3));
                writer.close();
            }

            final JSONTokener tokener = new JSONTokener(fis);
            object = new JSONObject(tokener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
            ex.printStackTrace();
        }

        this.reloadSettings = reloadSettings;

    }

    Json(final File file) {

        try {

            load(file);

            this.file = file;


            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException |
                    NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(2));
                writer.close();
            }

            JSONTokener tokener = new JSONTokener(fis);
            object = new JSONObject(tokener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
            ex.printStackTrace();
        }


    }

    //TODO IMMER auf TODOS überprüfen.


    /**
     * Sets a value to the json if the file doesn't already contain the value (Not mix up with Bukkit addDefault)
     * Uses {@link JSONObject}
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

        if (reloadSettings.equals(ReloadSettings.manually))
            return;

        if (reloadSettings.equals(ReloadSettings.intelligent))
            if (!FileUtils.hasChanged(file, lastModified))
                return;

        update();

    }


    public Object get(final String key) {

        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        return getObject(finalKey);
    }

    private Object getObject(final String key) {
        if (!has(key))
            return null;

        if (key.contains(".")) {
            return HashMapUtil.contains(key, object.toMap()) ? HashMapUtil.get(key, object.toMap()) : null;
        }
        return object.has(key) ? object.get(key) : null;
    }


    /**
     * Gets a long from a JSON-File
     * Uses {@link JSONObject}
     *
     * @param key Path to long in JSON-FILE
     * @return long from JSON
     */

    @Override
    public long getLong(String key) {
        reload();
        if (!contains(key))
            return 0;

        return (get(key) instanceof Integer) ? (long) (int) get(key) : (long) get(key);//TrobleShooting: Integer not castable to Long

    }

    /**
     * Get a double from a JSON-File
     * Uses {@link JSONObject}
     *
     * @param key Path to double in JSON-File
     * @return Double from JSON
     */

    @Override
    public double getDouble(String key) {
        reload();
        if (!contains(key))
            return 0;


        return (get(key) instanceof Integer) ? (double) (int) get(key) : (double) get(key);//TrobleShooting: Integer not castable to Double
        // -> Wrapper class

    }

    /**
     * Get a float from a JSON-File
     * Uses {@link JSONObject}
     *
     * @param key Path to float in JSON-File
     * @return Float from JSON
     */


    @Override
    public float getFloat(String key) {

        reload();

        if (!contains(key))
            return 0;

        if (key.contains(".")) {
            if (get(key) instanceof Double) {
                return (float) (double) get(key);
            }
            return (get(key) instanceof Integer) ? (float) (int) get(key) : (int) get(key);//TrobleShooting: Integer not castable to Double -> Wrapper class
            //
        }
        return (object.get(key) instanceof Integer) ? (float) (int) object.get(key) : (float) object.get(key);

    }


    /**
     * Gets a int from a JSON-File
     * <p>
     * Uses {@link JSONObject}
     *
     * @param key Path to int in JSON-File
     * @return Int from JSON
     */
    @Override
    public int getInt(String key) {
        reload();

        if (!contains(key))
            return 0;

        return (int) get(key);

    }

    /**
     * Get a byte from a JSON-File
     * Uses {@link JSONObject}
     *
     * @param key Path to byte in JSON-File
     * @return Byte from JSON
     */

    @Override
    public byte getByte(String key) {
        reload();

        if (!contains(key))
            return 0;


        return (byte) get(key);

    }

    /**
     * Get a boolean from a JSON-File
     * Uses {@link JSONObject}
     *
     * @param key Path to boolean in JSON-File
     * @return Boolean from JSON
     */

    @Override
    public boolean getBoolean(String key) {
        reload();

        if (!contains(key))
            return false;

        return (boolean) get(key);

    }


    /**
     * Get String List
     * Uses {@link JSONObject}
     *
     * @param key Path to String  in Json-File
     * @return String from Json
     */

    @Override
    public String getString(String key) {
        reload();

        if (!contains(key))
            return null;


        return (String) get(key);

    }


    /**
     * Get a List from a JSON-File by key
     * Uses {@link YamlObject}
     *
     * @param key Path to StringList in JSON-File
     * @return String-List
     */

    @Override
    public List<?> getList(final String key) {
        reload();
        if (!contains(key))
            return new ArrayList<>();


        final Object object = get(key);
        final JSONArray ja = new JSONArray(object.toString());
        List<Object> list = new ArrayList<>();
        for (Object a : ja) {
            list.add(a);
        }
        return list;

    }

    /**
     * Get a String-List from a JSON-File by key
     * Uses {@link JSONObject}
     *
     * @param key Path to String List in YAML-File
     * @return String-List
     */

    @Override
    public List<String> getStringList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();


        return (List<String>) getList(key);

    }

    /**
     * Get a Integer-List from a JSON-File by key
     * Uses {@link JSONObject}
     *
     * @param key Path to Integer List in JSON-File
     * @return Integer-List
     */

    @Override
    public List<Integer> getIntegerList(String key) {
        reload();
        if (!contains(key))
            return new ArrayList<>();

        return (List<Integer>) getList(key);
    }

    /**
     * Get a Byte-List from a JSON-File by key
     * Uses {@link JSONObject}
     *
     * @param key Path to Byte List in JSON-File
     * @return Byte-List
     */

    @Override
    public List<Byte> getByteList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<Byte>) getList(key);
    }


    /**
     * Get a Long-List from a JSON-File by key
     * Uses {@link JSONObject}
     *
     * @param key Path to Long List in JSON-File
     * @return Long-List
     */

    @Override
    public List<Long> getLongList(String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        return (List<Long>) getList(key);
    }

    /**
     * Gets a Map by key
     * Although used to get nested objects {@link Json}
     *
     * @param key Path to Map-List in JSON
     * @return Map
     */

    @Override
    public Map getMap(String key) {
        key = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return getMapWithoutPath(key);
    }

    private Map getMapWithoutPath(final String key) {
        reload();

        if (!has(key))
            return new HashMap();

        Object map;
        try {
            map = getObject(key);
        } catch (JSONException e) {
            return new HashMap<>();
        }
        if (map instanceof Map) {
            return (Map<?, ?>) object.get(key);
        } else if (map instanceof JSONObject) {
            return JsonUtil.jsonToMap((JSONObject) map);
        }
        throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
    }


    @Override
    public void set(final String key, final Object value) {

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            reload();

            if (finalKey.contains(".")) {

                final Map map = HashMapUtil.stringToMap(finalKey, value, object.toMap());

                object = new JSONObject(map);
                try {
                    Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                    writer.write(object.toString(3));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            object.put(finalKey, value);
            try {
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(2));
                writer.close();
            } catch (IOException e) {
                System.err.println("Couldn' t set " + finalKey + " " + value);
                e.printStackTrace();
            }
        }
    }


    @Override
    public <T> T getOrSetDefault(final String path, T def) {
        if (!contains(path)) {
            set(path, def);
            return def;
        } else {
            return (T) get(path);
        }
    }

    @Override
    public void update() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException | NullPointerException e) {
            System.err.println("Exception while reading Json");
            e.printStackTrace();
        }
        final JSONTokener tokener = new JSONTokener(fis);
        object = new JSONObject(tokener);
    }


    private boolean has(final String key) {
        reload();
        if (key.contains("."))
            return HashMapUtil.contains(key, object.toMap());
        return object.has(key);
    }

    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return has(finalKey);
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }


}
