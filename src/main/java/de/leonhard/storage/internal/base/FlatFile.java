package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public abstract class FlatFile implements Comparable<FlatFile> {
    protected FlatFile flatFileInstance = this;
    protected File file;
    protected long lastModified;
    protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
    private FileType fileType;


    /**
     * Creates an empty .yml or .json file.
     *
     * @param name     Name of the file
     * @param path     Absolute path where the file should be created
     * @param fileType .yml/.json  Uses the Enum FileType
     * @return true if file was created.
     */
    protected final synchronized boolean create(final String name, final String path, final FileType fileType) {
        this.fileType = fileType;
        file = new File(path, name + "." + fileType);
        if (file.exists()) {
            lastModified = System.currentTimeMillis();
            return false;
        } else {
            try {
                if (file.getAbsoluteFile().getParentFile().exists() || file.getAbsoluteFile().getParentFile().mkdirs()) {
                    if (!file.createNewFile()) {
                        throw new IOException();
                    }
                }
            } catch (IOException ex) {
                System.err.println("Exception while creating File '" + file.getName() + "'");
                System.err.println("Path: '" + file.getAbsolutePath() + "'");
                ex.printStackTrace();
            }
            lastModified = System.currentTimeMillis();
            return true;
        }
    }

    protected final synchronized boolean create(final File file) {
        this.fileType = FileType.getFileType(file);
        this.file = file;
        if (this.file.exists()) {
            lastModified = System.currentTimeMillis();
            return false;
        } else {
            try {
                if (this.file.getAbsoluteFile().getParentFile().exists() || file.getAbsoluteFile().getParentFile().mkdirs()) {
                    if (!this.file.createNewFile()) {
                        throw new IOException();
                    }
                }
            } catch (IOException ex) {
                System.err.println("Exception while creating File '" + file.getName() + "'");
                System.err.println("Path: '" + file.getAbsolutePath() + "'");
                ex.printStackTrace();
            }
            lastModified = System.currentTimeMillis();
            return true;
        }
    }

    protected final boolean shouldReload() {
        if (reloadSettings.equals(ReloadSettings.AUTOMATICALLY)) {
            return true;
        } else if (reloadSettings.equals(ReloadSettings.INTELLIGENT)) {
            return hasChanged();
        } else {
            return false;
        }
    }

    public boolean hasChanged() {
        return FileUtils.hasChanged(file, lastModified);
    }

    public final File getFile() {
        return file;
    }

    public final String getFilePath() {
        return file.getAbsolutePath();
    }

    @SuppressWarnings("unused")
    public final FileType getFileType() {
        return fileType;
    }

    @Override
    public synchronized boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            FlatFile flatFile = (FlatFile) obj;
            return this.file.equals(flatFile.file)
                    && this.lastModified == flatFile.lastModified
                    && reloadSettings.equals(flatFile.reloadSettings)
                    && fileType.equals(flatFile.fileType);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public synchronized int compareTo(final FlatFile flatFile) {
        return this.file.compareTo(flatFile.file);
    }

    @Override
    public synchronized int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public synchronized String toString() {
        return this.file.getAbsolutePath();
    }
}