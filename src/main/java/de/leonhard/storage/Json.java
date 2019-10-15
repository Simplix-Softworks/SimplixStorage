package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings({"Duplicates", "unused", "WeakerAccess", "unchecked"})
@Getter
public class Json extends FlatFile implements StorageBase {
    @Setter
    private String pathPrefix;
    private JSONObject jsonObject;

    /**
     * Creates a .json file where you can put your data in.
     *
     * @param name Name of the .json file
     * @param path Absolute path, where the .json file should be created.
     */

    public Json(final String name, final String path) {

        try {
            create(name, path, FileType.JSON);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(getFile());
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (getFile().length() == 0) {
                jsonObject = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
                writer.write(jsonObject.toString(2));
                writer.close();
            }

            final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            jsonObject = new JSONObject(tokener);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + Objects.requireNonNull(getFile()).getName());
            ex.printStackTrace();
        }

        setReloadSettings(ReloadSettings.INTELLIGENT);
    }

    public Json(final String name, final String path, ReloadSettings reloadSettings) {

        try {
            create(name, path, FileType.JSON);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(getFile());
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (getFile().length() == 0) {
                jsonObject = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
                writer.write(jsonObject.toString(3));
                writer.close();
            }

            final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            jsonObject = new JSONObject(tokener);

        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + getName());
            ex.printStackTrace();
        }

        this.setReloadSettings(reloadSettings);

    }

    public Json(final File file) {

        try {
            create(file);

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(getFile());
            } catch (FileNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }

            if (getFile().length() == 0) {
                jsonObject = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
                writer.write(jsonObject.toString(2));
                writer.close();
            }
            this.setReloadSettings(ReloadSettings.INTELLIGENT);
            JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
            jsonObject = new JSONObject(tokener);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println(
                    "Error while creating file - Maybe wrong format - Try deleting the file " + getName());
            ex.printStackTrace();
        }

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
        if (shouldReload()) {
            update();
        }
    }

    @Override
    public Object get(final String key) {

        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        return getObject(finalKey);
    }

    private Object getObject(final String key) {
        if (!has(key)) {
            return null;
        }

        if (key.contains(".")) {
            return new FileData(jsonObject.toMap()).containsKey(key) ? new FileData(jsonObject.toMap()).get(key) : null;
        }
        return jsonObject.has(key) ? jsonObject.get(key) : null;
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

        if (!has(key)) {
            return new HashMap<>();
        }

        Object map;
        try {
            map = getObject(key);
        } catch (JSONException e) {
            return new HashMap<>();
        }
        if (map instanceof Map) {
            return (Map<?, ?>) jsonObject.get(key);
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

                JSONObject old = this.jsonObject;

                final FileData data = new FileData(jsonObject.toMap());

                data.insert(finalKey, value);

                jsonObject = new JSONObject(data.toMap());
                try {
                    if (old.toString().equals(jsonObject.toString()) && getFile().length() != 0) {
                        return;
                    }

                    write(jsonObject);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            jsonObject.put(finalKey, value);
            try {
                Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
                writer.write(jsonObject.toString(2));
                writer.close();
            } catch (IOException e) {
                System.err.println("Couldn' t set " + finalKey + " " + value);
                e.printStackTrace();
            }
        }
    }

    public void write(final JSONObject object) throws IOException {
        Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
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
            fis = new FileInputStream(getFile());
        } catch (FileNotFoundException | NullPointerException e) {
            System.err.println("Exception while reading Json");
            e.printStackTrace();
        }
        final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
        jsonObject = new JSONObject(tokener);
    }

    @Override
    public Set<String> singleLayerKeySet() {
        reload();
        return new FileData(jsonObject.toMap()).singleLayerKeySet();
    }

    @Override
    public Set<String> singleLayerKeySet(final String key) {
        reload();
        return new FileData(jsonObject.toMap()).singleLayerKeySet(key);
    }

    @Override
    public Set<String> keySet() {
        reload();
        return new FileData(jsonObject.toMap()).keySet();
    }

    @Override
    public Set<String> keySet(final String key) {
        reload();
        return new FileData(jsonObject.toMap()).keySet(key);
    }

    private boolean has(final String key) {
        reload();
        return new FileData(jsonObject.toMap()).containsKey(key);
    }

    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return has(finalKey);
    }

    @Override
    public void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final Map obj = jsonObject.toMap();
        obj.remove(finalKey);

        jsonObject = new JSONObject(obj);
        try {
            write(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Json json = (Json) obj;
            return this.jsonObject.equals(json.jsonObject)
                    && this.pathPrefix.equals(json.pathPrefix)
                    && super.equals(json.getFlatFileInstance());
        }
    }
}