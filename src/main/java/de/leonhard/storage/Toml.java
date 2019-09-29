package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.FlatFile;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Toml extends FlatFile implements StorageBase {
    private Map<String, Object> data;
    private String pathPrefix;
    private final ReloadSettings reloadSettings;

    public Toml(final String name, final String path) {
        try {
            create(path, name, FileType.TOML);
            this.file = super.file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = ReloadSettings.INTELLIGENT;
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

    public Toml(final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        this.reloadSettings = ReloadSettings.INTELLIGENT;
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
     * Checks wheter a key exists in the file
     *
     * @param key Key to check
     * @return Returned value
     */
    @Override
    public boolean contains(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        reload();
        return Utils.contains(finalKey, data);
    }

    public void write(final Map<String, Object> data) {
        try {
            com.electronwill.toml.Toml.write(data, file);
        } catch (IOException e) {
            System.err.println("Exception while writing data to file '" + file.getName() + "'");
            e.printStackTrace();
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

    @Override
    public Set<String> getKeySet() {
        reload();
        return data.keySet();
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

    @Override
    public void removeKey(final String key) {
        data.remove(key);
        write(data);
    }

    public void remove(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        final Map<String, Object> old = data;

        data = Utils.remove(old, finalKey);

        write(data);
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
