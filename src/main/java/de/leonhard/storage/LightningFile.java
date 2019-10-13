package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.FlatFile;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;

import java.io.File;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
class LightningFile extends FlatFile implements StorageBase, Comparable<LightningFile> {


    private Map<String, Object> data;

    public LightningFile(final String name, final String path) {
        create(name, path, FileType.LS);
        this.reloadSettings = ReloadSettings.INTELLIGENT;
    }

    public LightningFile(final String name, final String path, final ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
    }

    LightningFile(final File file) {
        create(file);
    }

    public String getName() {
        return this.file.getName();
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
        if (obj != null && this.getClass() == obj.getClass()) {
            LightningFile lightningFile = (LightningFile) obj;
            return this.file.equals(lightningFile.file);
        } else {
            return false;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final LightningFile lightningFile) {
        return this.file.compareTo(lightningFile.file);
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