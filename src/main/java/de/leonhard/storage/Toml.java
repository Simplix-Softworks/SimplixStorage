package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.base.StorageCreator;
import de.leonhard.storage.comparator.Comparator;
import de.leonhard.storage.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess", "ResultOfMethodCallIgnored"})
public class Toml extends StorageCreator implements StorageBase, Comparator {
    private final ReloadSettings reloadSettings;
    private Map<String, Object> data;
    private File file;
    private String pathPrefix;

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

    public String getName() {
        return this.file.getName();
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
     * Returns the FilePath as String
     *
     * @return FilePath as String
     */
    @Override
    public String getFilePath() {
        return null;
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
     * Reloads the file when needed see {@link ReloadSettings} for deeper
     * information
     */
    private void reload() {
        if (shouldNotReload(reloadSettings))
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return this.file.equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(File pathname) {
        return this.file.compareTo(pathname);
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