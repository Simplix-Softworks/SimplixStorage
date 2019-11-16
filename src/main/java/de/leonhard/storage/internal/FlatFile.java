package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

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

	protected FlatFile(String name, String path, FileType fileType) {
		this.fileType = fileType;
		if (path == null || path.isEmpty()) {
			this.file = new File(FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
		} else {
			this.file = new File(path, FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
		}
	}

	protected FlatFile(File file, FileType fileType) {
		if (!fileType.getExtension().equals(FileUtils.getExtension(file))) {
			throw new IllegalStateException("Invalid FileType for File '" + file.getName() + "'");
		}
		this.file = file;
	}


	// ----------------------------------------------------------------------------------------------------
	// Abstract methods (Reading & Writing)
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Forces Re-read/load the content of our flat file
	 * Should be used to put the data from the file to our FileData
	 */
	protected abstract void reRead();

	/**
	 * Write our data to file
	 *
	 * @param data Our data
	 */
	protected abstract void write(final FileData data) throws IOException;

	// ----------------------------------------------------------------------------------------------------
	//  Creating out file
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Creates an empty .yml or .json file.
	 *
	 * @return true if file was created.
	 */
	protected final boolean create() {
		return createFile(this.file);
	}

	@Synchronized
	private boolean createFile(File file) {
		if (file.exists()) {
			lastModified = System.currentTimeMillis();
			return false;
		} else {
			FileUtils.getAndMake(file);
			lastModified = System.currentTimeMillis();
			return true;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Overridden methods from IStorage
	// ---------------------------------------------------------------------------------------------------->

	@Override
	@Synchronized
	public void set(String key, Object value) {
		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		fileData.insert(finalKey, value);
		write();
		lastModified = System.currentTimeMillis();
	}

	@Override
	public Object get(String key) {
		reloadIfNeeded();
		String finalKey = pathPrefix == null ? key : pathPrefix + "." + key;
		return fileData.get(finalKey);
	}

	/**
	 * Checks whether a key exists in the file
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
		reloadIfNeeded();
		return fileData.singleLayerKeySet();
	}

	@Override
	public Set<String> singleLayerKeySet(String key) {
		reloadIfNeeded();
		return fileData.singleLayerKeySet(key);
	}

	@Override
	public Set<String> keySet() {
		reloadIfNeeded();
		return fileData.keySet();
	}

	@Override
	public Set<String> keySet(String key) {
		reloadIfNeeded();
		return fileData.keySet(key);
	}

	@Override
	@Synchronized
	public void remove(String key) {
		fileData.remove(key);
	}

	// ----------------------------------------------------------------------------------------------------
	// Pretty nice utility methods for FlatFile
	// ----------------------------------------------------------------------------------------------------

	public final String getName() {
		return this.file.getName();
	}

	public final String getFilePath() {
		return file.getAbsolutePath();
	}

	@Synchronized
	public void replace(CharSequence target, CharSequence replacement) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());
		List<String> result = new ArrayList<>();
		for (String line : lines) {
			result.add(line.replace(target, replacement));
		}
		Files.write(file.toPath(), result);
	}

	public void write() {
		try {
			write(fileData);
		} catch (IOException ex) {
			System.err.println("Exception writing to file '" + getName() + "'");
			System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
		lastModified = System.currentTimeMillis();
	}

	public void removeAll(final String... keys) {
		for (String key : keys) {
			fileData.remove(key);
		}
		write();
	}

	public final boolean hasChanged() {
		return FileUtils.hasChanged(file, lastModified);
	}


	public final void forceReload() {
		reRead();
		lastModified = System.currentTimeMillis();
	}

	// ----------------------------------------------------------------------------------------------------
	// Internal stuff
	// ----------------------------------------------------------------------------------------------------

	protected final void reloadIfNeeded() {
		if (shouldReload()) {
			forceReload();
		}
	}

	//Should the file be re-read before the next get() operation?
	protected boolean shouldReload() {
		if (ReloadSettings.AUTOMATICALLY.equals(reloadSettings)) {
			return true;
		} else if (ReloadSettings.INTELLIGENT.equals(reloadSettings)) {
			return FileUtils.hasChanged(file, lastModified);
		} else {
			return false;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Misc
	// ----------------------------------------------------------------------------------------------------

	@Override
	public int compareTo(FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof File) {
			return obj.equals(file);
		}
		if (obj instanceof FlatFile) {
			return ((FlatFile) obj).file.equals(file);
		}
		return false;
	}
}