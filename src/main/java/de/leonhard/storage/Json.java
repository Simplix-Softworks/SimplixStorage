package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.JsonUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Json extends FlatFile {

    public Json(String name, String path) {
        this(name, path, null);
    }

    public Json(String name, String path, ReloadSettings reloadSettings) {
        super(name, path, FileType.JSON);
        if (create() || file.length() == 0) {
            try (Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()))) {
                writer.write(new JSONObject().toString(2));
            } catch (Exception ex) {
                System.err.println("Error creating JSON '" + file.getName() + "'");
                System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
                ex.printStackTrace();
            }
        }
        if (reloadSettings != null) {
            this.reloadSettings = reloadSettings;
        }
        reRead();
    }

    public Json(File file) {
        super(file, FileType.JSON);
        create();
        reRead();
    }


    // ----------------------------------------------------------------------------------------------------
    // Methods to override (Points where JSON is unspecific for typical FlatFiles)
    // ----------------------------------------------------------------------------------------------------

    /**
     * Gets a Map by key Although used to get nested objects {@link Json}
     *
     * @param key Path to Map-List in JSON
     * @return Map
     */
    @Override
    public Map getMap(String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        if (!contains(finalKey)) {
            return new HashMap();
        } else {
            final Object map = get(key);
            if (map instanceof Map) {
                return (Map<?, ?>) fileData.get(key);
            } else if (map instanceof JSONObject) {
                return JsonUtils.jsonToMap((JSONObject) map);
            }
            //Exception in casting
            throw new IllegalArgumentException("ClassCastEx: Json contains key: '" + key + "' but it is not a Map");
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected void reRead() {
        final JSONTokener jsonTokener = new JSONTokener(FileUtils.createInputStream(file));
        fileData = new FileData(new JSONObject(jsonTokener), dataType);
    }

    @Override
    protected void write(FileData data) throws IOException {
        try (Writer writer = FileUtils.createWriter(file)) {
            writer.write(data.toJsonObject().toString(3));
            writer.flush();
        }
    }
}