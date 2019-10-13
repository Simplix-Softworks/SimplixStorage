package de.leonhard.storage;


import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.FlatFile;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.util.FileData;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Toml extends FlatFile implements StorageBase, Comparable<Toml> {
    private FileData fileData;
    private String pathPrefix;

    public Toml(final String name, final String path) {
        create(path, name, FileType.TOML);
        update();
    }

    public Toml(final String name, final String path, final ReloadSettings reloadSettings) {
        create(name, path, FileType.YAML);
        update();
    }

    public Toml(final File file) {
        create(file);
        this.file = file;
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
    public void set(final String key, final Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final String old = fileData.toString();

        fileData.insert(finalKey, value);

        if (old.equals(fileData.toString()))
            return;

        try {
            com.electronwill.toml.Toml.write(fileData.toMap(), file);
        } catch (IOException e) {
            System.err.println("Exception while writing to Toml file '" + file.getName() + "'");
            e.printStackTrace();
        }
    }

    @Override
    public Object get(final String key) {
        reload();
        return fileData.get(key);
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
        return fileData.containsKey(finalKey);
    }

    public void write(final Map<String, Object> data) {
        try {
            com.electronwill.toml.Toml.write(data, file);
        } catch (IOException e) {
            System.err.println("Exception while writing fileData to file '" + file.getName() + "'");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        try {
            fileData = new FileData(com.electronwill.toml.Toml.read(file));
        } catch (IOException e) {
            System.err.println("Exception while reading '" + file.getName() + "'");
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> singleLayerKeySet() {
        reload();
        return fileData.singleLayerKeySet();
    }

    @Override
    public Set<String> singleLayerKeySet(final String key) {
        reload();
        return fileData.singleLayerKeySet(key);
    }

    @Override
    public Set<String> keySet() {
        reload();
        return fileData.keySet();
    }

    @Override
    public Set<String> keySet(final String key) {
        reload();
        return fileData.keySet(key);
    }

    /**
     * Reloads the file when needed see {@link ReloadSettings} for deeper
     * information
     */
    private void reload() {
        if (shouldReload(reloadSettings)) {
            update();
        }
    }

    @Override
    public void remove(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        fileData.remove(finalKey);

        write(fileData.toMap());
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
            Toml toml = (Toml) obj;
            return this.file.equals(toml.file);
        } else {
            return false;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final Toml toml) {
        return this.file.compareTo(toml.file);
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