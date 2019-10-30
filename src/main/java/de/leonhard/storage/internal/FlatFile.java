package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.sections.FlatFileSection;
import de.leonhard.storage.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public abstract class FlatFile implements IStorage, Comparable<FlatFile> {
    @Setter
    protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
    protected FileData fileData = new FileData();
    protected File file;
    protected FileType fileType;
    @Setter
    protected String pathPrefix;
    private long lastModified;


    public FlatFile(String name, String path, FileType fileType) {
        this.fileType = fileType;
        if (path == null || path.isEmpty()) {
            this.file = new File(FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
        } else {
            this.file = new File(path, FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
        }
    }

    public FlatFile(File file, FileType fileType) {
        if (!fileType.getExtension().equals(FileUtils.getExtension(file))) {
            throw new IllegalStateException("Invalid FileType for File '" + file.getName() + "'");
        }
        this.file = file;
    }

    public final String getName() {
        return this.file.getName();
    }

    public final String getFilePath() {
        return file.getAbsolutePath();
    }

    public void replace(CharSequence target, CharSequence replacement) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.add(line.replace(target, replacement));
        }
        Files.write(file.toPath(), result);
    }

    public void reload() {
        if (shouldReload()) {
            forceReload();
        }
    }

    @Override
    public Object get(String key) {
        reload();
        String finalKey = pathPrefix == null ? key : pathPrefix + "." + key;
        return fileData.get(finalKey);
    }

    /**
     * Checks wheter a key exists in the file
     *
     * @param key Key to check
     * @return Returned value
     */
    @Override
    public boolean contains(String key) {
        String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return fileData.containsKey(finalKey);
    }

    @Override
    public Set<String> singleLayerKeySet() {
        reload();
        return fileData.singleLayerKeySet();
    }

    @Override
    public Set<String> singleLayerKeySet(String key) {
        reload();
        return fileData.singleLayerKeySet(key);
    }

    @Override
    public Set<String> keySet() {
        reload();
        return fileData.keySet();
    }

    @Override
    public Set<String> keySet(String key) {
        reload();
        return fileData.keySet(key);
    }

    public FlatFileSection getSection(final String path){
        return new FlatFileSection(this, path);
    }


	@Override
	public synchronized int compareTo(FlatFile flatFile) {
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

	@Override
	public synchronized boolean equals(Object obj) {
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

    /**
     * Reread the content of our flat file
     */
    protected abstract void forceReload();

    /**
     * Creates an empty .yml or .json file.
     *
     * @return true if file was created.
     */
    protected final synchronized boolean create() {
        return createFile(this.file);
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


    private boolean hasChanged() {
        return FileUtils.hasChanged(file, lastModified);
    }

    private synchronized boolean createFile(File file) {
        if (file.exists()) {
            lastModified = System.currentTimeMillis();
            return false;
        } else {
            FileUtils.getAndMake(file);
            lastModified = System.currentTimeMillis();
            return true;
        }
    }
}