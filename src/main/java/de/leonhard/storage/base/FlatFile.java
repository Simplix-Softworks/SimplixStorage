package de.leonhard.storage.base;

import de.leonhard.storage.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class FlatFile {
    protected File file;
    private FileType fileType;
    protected long lastModified;
    protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;


    /**
     * Creates an empty .yml or .json file.
     *
     * @param name     Name of the file
     * @param path     Absolute path where the file should be created
     * @param fileType .yml/.file  Uses the Enum FileType
     * @return true if file was created.
     */
    protected final synchronized boolean create(final String name, final String path, final FileType fileType) {
        this.fileType = fileType;
        file = new File(path, name + fileType.getExtension());
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

    protected final boolean shouldReload(final ReloadSettings reloadSettings) {
        if (reloadSettings.equals(ReloadSettings.MANUALLY)) {
            return true;
        }

        if (ReloadSettings.INTELLIGENT.equals(reloadSettings)) {
            return FileUtils.hasNotChanged(file, lastModified);
        }
        return false;
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
}