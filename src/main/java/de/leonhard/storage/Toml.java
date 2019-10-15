package de.leonhard.storage;


import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
@Getter
public class Toml extends FlatFile implements StorageBase {
    @Setter
    private String pathPrefix;
    private FileData fileData;

    public Toml(final String name, final String path) {
        create(name, path, FileType.TOML);
        update();
    }

    public Toml(final String name, final String path, final ReloadSettings reloadSettings) {
        create(name, path, FileType.YAML);
        update();
    }

    public Toml(final File file) {
        create(file);
        update();
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

        if (old.equals(fileData.toString())) {
            return;
        }

        try {
            com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
        } catch (IOException e) {
            System.err.println("Exception while writing to Toml file '" + getName() + "'");
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
            com.electronwill.toml.Toml.write(data, getFile());
        } catch (IOException e) {
            System.err.println("Exception while writing fileData to file '" + getName() + "'");
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        try {
            fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
        } catch (IOException e) {
            System.err.println("Exception while reading '" + getName() + "'");
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
        if (shouldReload()) {
            update();
        }
    }

    @Override
    public void remove(final String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        fileData.remove(finalKey);

        write(fileData.toMap());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Toml toml = (Toml) obj;
            return this.fileData.equals(toml.fileData)
                    && this.pathPrefix.equals(toml.pathPrefix)
                    && super.equals(toml.getFlatFileInstance());
        }
    }
}
