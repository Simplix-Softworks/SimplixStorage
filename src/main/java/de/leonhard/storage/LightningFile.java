package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;

import java.io.File;
import java.util.Set;

@SuppressWarnings("unused")
public class LightningFile extends FlatFile implements StorageBase {

    private FileData fileData;

    public LightningFile(final String name, final String path) {
        create(name, path, FileType.LS);
        setReloadSettings(ReloadSettings.INTELLIGENT);
    }

    public LightningFile(final String name, final String path, final ReloadSettings reloadSettings) {
        setReloadSettings(reloadSettings);
    }

    LightningFile(final File file) {
        create(file);
    }

    //added method for later implementation
    @Override
    public void update() {

    }

    @Override
    public Set<String> getKeySet() {
        return null;
    }

    @Override
    public void removeKey(String key) {

    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            LightningFile lightningFile = (LightningFile) obj;
            return this.fileData.equals(lightningFile.fileData)
                    && super.equals(lightningFile.getFlatFileInstance());
        }
    }
}