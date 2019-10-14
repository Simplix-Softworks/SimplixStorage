package de.leonhard.storage;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;

import java.io.File;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class LightningFile extends FlatFile implements StorageBase {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String, Object> data;

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
        //TODO
    }

    //added method for later implementation
    @Override
    public Set<String> singleLayerKeySet() {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> singleLayerKeySet(final String key) {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> keySet() {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> keySet(final String key) {
        return null;
    }

    //added method for later implementation
    @Override
    public void remove(final String key) {
        //TODO
    }

    //added method for later implementation
    @Override
    public void set(final String key, final Object value) {
        //TODO
    }

    @Override
    public boolean contains(final String key) {
        return false;
    }

    @Override
    public Object get(final String key) {
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
            return this.data.equals(lightningFile.data)
                    && super.equals(lightningFile.getFlatFileInstance());
        }
    }
}