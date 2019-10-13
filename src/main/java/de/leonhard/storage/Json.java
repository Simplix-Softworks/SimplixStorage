package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.FlatFile;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.utils.FileData;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"Duplicates", "unused", "WeakerAccess", "unchecked"})
public class Json extends FlatFile implements StorageBase, Comparable<Json> {
    private JSONObject object;
    private String pathPrefix;

    /**
     * Creates a .json file where you can put your data in.+
     *
     * @param name Name of the .json file
     * @param path Absolute path, where the .json file should be created.
     */

    public Json(final String name, final String path) {

        try {
            create(name, path, FileType.JSON);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(2));
                writer.close();
            }

            final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            object = new JSONObject(tokener);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + Objects.requireNonNull(file).getName());
            ex.printStackTrace();
        }

        this.reloadSettings = ReloadSettings.INTELLIGENT;
    }

    public Json(final String name, final String path, ReloadSettings reloadSettings) {

        try {
            create(name, path, FileType.JSON);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(3));
                writer.close();
            }

            final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            object = new JSONObject(tokener);

        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + Objects.requireNonNull(file).getName());
            ex.printStackTrace();
        }

        this.reloadSettings = reloadSettings;

    }

    public Json(final File file) {

        try {
            create(file);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (file.length() == 0) {
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
                writer.write(object.toString(2));
                writer.close();
            }
            this.reloadSettings = ReloadSettings.INTELLIGENT;
            JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            object = new JSONObject(tokener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
            ex.printStackTrace();
        }

    }

    public String getName() {
        return this.file.getName();
    }

    /**
     * Sets a value to the json if the file doesn't already contain the value
     * (Not mix up with Bukkit addDefault) Uses {@link JSONObject}
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
        if (!(ReloadSettings.MANUALLY.equals(reloadSettings)
                || (ReloadSettings.INTELLIGENT.equals(reloadSettings)
                && FileUtils.hasNotChanged(file, lastModified)))) {
            update();
        }
    }

    @Override
    public Object get(final String key) {

        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        return getObject(finalKey);
    }

    private Object getObject(final String key) {
        if (!has(key))
            return null;

        if (key.contains(".")) {
            return new FileData(object.toMap()).containsKey(key) ? new FileData(object.toMap()).get(key) : null;
        }
        return object.has(key) ? object.get(key) : null;
    }

    /**
     * Gets a Map by key Although used to get nested objects {@link Json}
     *
     * @param key Path to Map-List in JSON
     * @return Map
     */

    @Override
    public Map getMap(final String key) {
        String tempKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        if (!contains(tempKey)) {
            return new HashMap();
        } else {
            return getMapWithoutPath(tempKey);
        }
    }

    private Map getMapWithoutPath(final String key) {
        reload();

        if (!has(key))
            return new HashMap<>();

        Object map;
        try {
            map = getObject(key);
        } catch (JSONException e) {
            return new HashMap<>();
        }
        if (map instanceof Map) {
            return (Map<?, ?>) object.get(key);
        } else if (map instanceof JSONObject) {
            return JsonUtils.jsonToMap((JSONObject) map);
        }
        throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
    }

    @Override
    public void set(final String key, final Object value) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            reload();

            if (finalKey.contains(".")) {

                JSONObject old = this.object;

                final FileData data = new FileData(object.toMap());

                data.insert(finalKey, value);

                object = new JSONObject(data.toMap());
                try {
                    if (old.toString().equals(object.toString()) && file.length() != 0)
                        return;

                    write(object);

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

    public void write(final JSONObject object) throws IOException {
        Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
        writer.write(object.toString(3));
        writer.close();
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
        final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
        object = new JSONObject(tokener);
    }

    @Override
    public Set<String> singleLayerKeySet() {
        reload();
        return new FileData(object.toMap()).singleLayerKeySet();
    }

    @Override
    public Set<String> singleLayerKeySet(final String key) {
        reload();
        return new FileData(object.toMap()).singleLayerKeySet(key);
    }

    @Override
    public Set<String> keySet() {
        reload();
        return new FileData(object.toMap()).keySet();
    }

    @Override
    public Set<String> keySet(final String key) {
        reload();
        return new FileData(object.toMap()).keySet(key);
    }

    private boolean has(final String key) {
        reload();
        return new FileData(object.toMap()).containsKey(key);
    }

    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return has(finalKey);
    }

    @Override
    public void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final Map obj = object.toMap();
        obj.remove(finalKey);

        object = new JSONObject(obj);
        try {
            write(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(final String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            Json json = (Json) obj;
            return this.file.equals(json.file);
        } else {
            return false;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final Json json) {
        return this.file.compareTo(json.file);
    }

    @Override
    public int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public String toString() {
        return this.file.getAbsolutePath();
    }
}