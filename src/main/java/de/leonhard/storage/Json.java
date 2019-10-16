package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.utils.FileUtils;
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
    private FileData fileData;


    public Json(final String name, final String path) {
        try {
            create(name, path, FileType.JSON);
            if (getFile().length() == 0) {

                Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
                writer.write(new JSONObject().toString(2));
                writer.close();
            }

            update();
        } catch (final Exception ex) {
            System.err.println("Error creating JSON '" + file.getName() + "'");
            System.err.println("In '" + file.getAbsolutePath() + "'");
            ex.printStackTrace();
        }
    }

    public Json(final File file) {
        this(file.getName().replace(".yml", ""), file.getAbsolutePath());
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
            return new FileData(fileData.toMap()).containsKey(key) ? new FileData(fileData.toMap()).get(key) : null;
        }
        return fileData.containsKey(key) ? fileData.get(key) : null;
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
            return (Map<?, ?>) fileData.get(key);
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

            FileData old = new FileData(fileData.toMap());


            fileData.insert(key, value);


            try {
                if (old.toString().equals(fileData.toString()) && getFile().length() != 0) {
                    return;
                }
                write(fileData.toJsonObject());
            } catch (IOException e) {
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
        final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(FileUtils.createNewInputStream(file)));
        fileData = new FileData(new JSONObject(tokener));
    }

    @Override
    public Set<String> singleLayerKeySet() {
        reload();
        return new FileData(fileData.toMap()).singleLayerKeySet();
    }

    @Override
    public Set<String> singleLayerKeySet(final String key) {
        reload();
        return new FileData(fileData.toMap()).singleLayerKeySet(key);
    }

    @Override
    public Set<String> keySet() {
        reload();
        return new FileData(fileData.toMap()).keySet();
    }

    @Override
    public Set<String> keySet(final String key) {
        reload();
        return new FileData(fileData.toMap()).keySet(key);
    }

    private boolean has(final String key) {
        reload();
        return new FileData(fileData.toMap()).containsKey(key);
    }

    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return has(finalKey);
    }

    @Override
    public void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final Map obj = fileData.toMap();
        obj.remove(finalKey);

        fileData = new FileData(new JSONObject(obj));
        try {
            write(fileData.toJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final Json getJsonInstance() {
        return this;
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Json json = (Json) obj;
            return this.fileData.equals(json.fileData)
                    && this.pathPrefix.equals(json.pathPrefix)
                    && super.equals(json.getFlatFileInstance());
        }
    }
}