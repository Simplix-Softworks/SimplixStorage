package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Set;

@Getter
public abstract class FlatFile implements StorageBase, Comparable<FlatFile> {
	@Setter
	protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
	protected FileData fileData = new FileData();
	protected File file;
	protected FileType fileType;
	private long lastModified;

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
		this.file = new File(path, name + "." + fileType);
		return generateFile(file);
	}

	protected final synchronized boolean create(final File file) {
		this.fileType = FileType.getFileType(file);
		this.file = file;
		return generateFile(file);
	}

	private synchronized boolean generateFile(File file) {
		if (file.exists()) {
			lastModified = System.currentTimeMillis();
			return false;
		} else {
			FileUtils.getAndMake(file);
			lastModified = System.currentTimeMillis();
			return true;
		}
	}

	@Override
	public void reload() {
		if (shouldReload()) {
			update();
		}
	}

	protected abstract void update();

	@Override
	public Set<String> singleLayerKeySet() {
		reload();
		return fileData.singleLayerKeySet();
	}

	@Override
	public Set<String> singleLayerKeySet(final String key) {
		reload();
		return fileData.singleLayerKeySet(key);
	}

	@Override
	public Set<String> keySet() {
		reload();
		return fileData.keySet();
	}

	@Override
	public Set<String> keySet(final String key) {
		reload();
		return fileData.keySet(key);
	}

	public final boolean shouldReload() {
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

	public final String getName() {
		return this.file.getName();
	}

	public final String getFilePath() {
		return file.getAbsolutePath();
	}

	protected final FlatFile getFlatFileInstance() {
		return this;
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