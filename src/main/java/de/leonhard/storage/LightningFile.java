package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.base.StorageCreator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
class LightningFile extends StorageCreator implements StorageBase, Comparable<LightningFile> {


    private final ReloadSettings reloadSettings;
    private Map<String, Object> data;
    private File file;

    public LightningFile(final String name, final String path) {
        try {
            create(path, name, FileType.LS);
        } catch (IOException e) {
            System.err.println("Exception while creating '" + file.getName() + "'");
            e.printStackTrace();
        }
        this.reloadSettings = ReloadSettings.INTELLIGENT;
    }

    public LightningFile(final String name, final String path, final ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
    }

    LightningFile(final File file) {
        this.file = file;
        this.reloadSettings = ReloadSettings.INTELLIGENT;
    }

    public String getName() {
        return this.file.getName();
    }

    public File getFile() {
        return this.file;
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
