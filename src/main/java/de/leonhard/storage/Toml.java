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
        this(name, path);
        this.reloadSettings = reloadSettings;
    }

    public Toml(final File file) {
        create(file);
        update();
    }

    /**
     * Set an object to your file
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
    public Set<String> getKeySet() {
        reload();
        return data.keySet();
    }

    /**
     * Reloads the file when needed see {@link ReloadSettings}
     * for deeper information
     */
    private void reload() {
        if (shouldReload()) {
            update();
        }
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