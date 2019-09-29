package de.leonhard.storage.base;

import com.sun.tools.javac.util.StringUtils;
import de.leonhard.storage.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class FlatFile {
    protected File file;
    private FileType fileType;
    protected long lastModified;

    /**
     * Creates an empty .yml or .json file.
     *
     * @param path     Absolute path where the file should be created
     * @param name     Name of the file
     * @param fileType .yml/.file  Uses the Enum FileType
     * @throws IOException Exception thrown if file could not be created.
     */
    protected final synchronized void create(final String path, final String name, final FileType fileType) throws IOException {
        if(path == null || path.isEmpty()){
            file = new File(name + fileType.getExtension());
            file.createNewFile();
            return;
        }
        this.fileType = fileType;
        String fixedPath = path.replace("\\", "/"); //Windows

        file = FileUtil.getOrCreate(new File(fixedPath + File.separator + name + fileType.getExtension()));

        if (!file.exists()) {
            file.createNewFile();
        }
        lastModified = System.currentTimeMillis();
    }

    protected final synchronized void load(final File file) {
        this.file = file;
    }

    protected final boolean shouldReload(final ReloadSettings reloadSettings) {
        if (reloadSettings.equals(ReloadSettings.MANUALLY))
            return true;

        if (ReloadSettings.INTELLIGENT.equals(reloadSettings))
            return FileUtil.hasNotChanged(file, lastModified);
        return false;
    }

    public final File getFile() {
        return file;
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    public final FileType getFileType() {
        return fileType;
    }
}
