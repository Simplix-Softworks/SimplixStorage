package de.leonhard.storage.base;

import de.leonhard.storage.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public abstract class StorageCreator {
    protected File file;
    protected long lastModified;
    private FileType fileType;

    /**
     * Creates an empty .yml or .json file.
     *
     * @param path     Absolute path where the file should be created
     * @param name     Name of the file
     * @param fileType .yml/.file Uses the Enum FileType
     * @throws IOException Exception thrown if file could not be created.
     */

    protected synchronized boolean create(final String path, final String name, final FileType fileType)
            throws IOException {
        this.fileType = fileType;
        initFile(path, name, fileType);

        return createFile(this.file);
    }

    protected synchronized boolean create(final String path, final String name)
            throws IOException {
        this.fileType = FileType.getFileType(name);
        initFile(path, name, fileType);

        return createFile(file);
    }

    protected synchronized boolean create(final File file)
            throws IOException {
        this.fileType = FileType.getFileType(file);
        this.file = file;
        return createFile(this.file);
    }

    private void initFile(String path, String name, FileType fileType) {
        if (path == null || path.equals("")) {
            this.file = new File(name + "." + fileType.getExtension());
        } else {
            String fixedPath = path.replace("\\", "/");
            ArrayList<String> parts = new ArrayList<>(Arrays.asList(fixedPath.split("/")));
            StringBuilder datafolder = new StringBuilder();

            for (String part : parts) {
                datafolder.append(part).append("/");
            }
            File folders = new File(datafolder.toString());
            if (!folders.exists()) {
                folders.mkdirs();
            }

            this.file = new File(fixedPath + File.separator + name + "." + fileType.getExtension());
        }
    }

    private boolean createFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            lastModified = System.currentTimeMillis();
            return true;
        } else {
            lastModified = System.currentTimeMillis();
            return false;
        }
    }

    protected synchronized void load(final File file) {
        this.file = file;
    }

    protected boolean shouldNotReload(ReloadSettings reloadSettings) {
        if (reloadSettings.equals(ReloadSettings.INTELLIGENT)) {
            return !FileUtils.hasNotChanged(file, lastModified);
        } else {
            return !reloadSettings.equals(ReloadSettings.MANUALLY);
        }
    }

    public File getFile() {
        return file;
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    public FileType getFileType() {
        return fileType;
    }
}