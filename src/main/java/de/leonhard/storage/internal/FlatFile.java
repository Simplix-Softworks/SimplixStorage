package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.sections.FlatFileSection;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public abstract class FlatFile implements DataStorage, Comparable<FlatFile> {
	protected final File file;
	protected final FileType fileType;
	@Setter
	protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
	protected DataType dataType = DataType.UNSORTED;
	protected FileData fileData;
	@Setter
	protected String pathPrefix;
	private long lastLoaded;

	protected FlatFile(final String name, final String path, final FileType fileType) {
		Valid.checkBoolean(!name.isEmpty(), "Name mustn't be empty");
		this.fileType = fileType;
		if (path == null || path.isEmpty()) {
			file = new File(FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
		} else {
			final String fixedPath = path.replace("\\", "/");
			file = new File(fixedPath + File.separator + name + "." + fileType.getExtension());
		}
	}

	protected FlatFile(final File file, final FileType fileType) {
		Valid.notNull(file);
		Valid.notNull(fileType);
		this.file = file;
		this.fileType = fileType;

		Valid.checkBoolean(!(fileType == FileType.fromExtension(file)),
				"Invalid file-extension for file type: '" + fileType + "'");

	}

	/**
	 * This constructor should only be used to
	 * store for example YAML-LIKE data in a .db file
	 * <p>
	 * Therefor no validation is possible.
	 * Might be unsafe.
	 */
	protected FlatFile(final File file) {
		this.file = file;
		//Might be null
		fileType = FileType.fromFile(file);
	}


	// ----------------------------------------------------------------------------------------------------
	// Abstract methods (Reading & Writing)
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Forces Re-read/load the content of our flat file
	 * Should be used to put the data from the file to our FileData
	 */
	protected abstract Map<String, Object> readToMap() throws IOException;

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
		return createFile(file);
	}

	@Synchronized
	private boolean createFile(final File file) {
		if (file.exists()) {
			lastLoaded = System.currentTimeMillis();
			return false;
		} else {
			FileUtils.getAndMake(file);
			lastLoaded = System.currentTimeMillis();
			return true;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Overridden methods from DataStorage
	// ---------------------------------------------------------------------------------------------------->

	@Override
	@Synchronized
	public void set(final String key, final Object value) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		fileData.insert(finalKey, value);
		write();
		lastLoaded = System.currentTimeMillis();
	}

	@Override
	public Object get(final String key) {
		reloadIfNeeded();
		final String finalKey = pathPrefix == null ? key : pathPrefix + "." + key;
		return fileData.get(finalKey);
	}

	/**
	 * Checks whether a key exists in the file
	 *
	 * @param key Key to check
	 * @return Returned value
	 */
	@Override
	public boolean contains(final String key) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		return fileData.containsKey(finalKey);
	}

	@Override
	public Set<String> singleLayerKeySet() {
		reloadIfNeeded();
		return fileData.singleLayerKeySet();
	}

	@Override
	public Set<String> singleLayerKeySet(final String key) {
		reloadIfNeeded();
		return fileData.singleLayerKeySet(key);
	}

	@Override
	public Set<String> keySet() {
		reloadIfNeeded();
		return fileData.keySet();
	}

	@Override
	public Set<String> keySet(final String key) {
		reloadIfNeeded();
		return fileData.keySet(key);
	}

	@Override
	@Synchronized
	public void remove(final String key) {
		fileData.remove(key);
	}

	// ----------------------------------------------------------------------------------------------------
	// More advanced & FlatFile specific operations to add data.
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Method to insert the data of a map to our FlatFile
	 *
	 * @param map Map to insert.
	 */
	public final void putAll(final Map<String, Object> map) {
		fileData.putAll(map);
		write();
	}

	/**
	 * @return The data of our file as a Map<String, Object>
	 */
	public final Map<String, Object> getData() {
		return getFileData().toMap();
	}

	// For performance separated from get(String key)
	public final List<Object> getAll(final String... keys) {
		final List<Object> result = new ArrayList<>();
		reloadIfNeeded();

		for (final String key : keys) {
			result.add(get(key));
		}

		return result;
	}

	public void removeAll(final String... keys) {
		for (final String key : keys) {
			fileData.remove(key);
		}
		write();
	}

	// ----------------------------------------------------------------------------------------------------
	// Pretty nice utility methods for FlatFile
	// ----------------------------------------------------------------------------------------------------

	public final String getName() {
		return file.getName();
	}

	public final String getFilePath() {
		return file.getAbsolutePath();
	}

	@Synchronized
	public void replace(final CharSequence target, final CharSequence replacement) throws IOException {
		final List<String> lines = Files.readAllLines(file.toPath());
		final List<String> result = new ArrayList<>();
		for (final String line : lines) {
			result.add(line.replace(target, replacement));
		}
		Files.write(file.toPath(), result);
	}

	public void write() {
		try {
			write(fileData);
		} catch (final IOException ex) {
			System.err.println("Exception writing to file '" + getName() + "'");
			System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
		lastLoaded = System.currentTimeMillis();
	}

	public final boolean hasChanged() {
		return FileUtils.hasChanged(file, lastLoaded);
	}

	public final void forceReload() {
		try {
			if (fileData == null) {
				fileData = new FileData(readToMap(), dataType);
			} else {
				fileData.loadData(readToMap());
			}
		} catch (final IOException ex) {
			final String fileName = fileType == null ? "File" : fileType.name().toLowerCase(); // fileType might be null
			System.err.println("Error reloading " + fileName + " '" + getName() + "'");
			System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
		lastLoaded = System.currentTimeMillis();
	}

	public final void clear() {
		fileData.clear();
		write();
	}

	// ----------------------------------------------------------------------------------------------------
	// Internal stuff
	// ----------------------------------------------------------------------------------------------------

	protected final void reloadIfNeeded() {
		if (shouldReload()) {
			forceReload();
		}
	}

	// Should the file be re-read before the next get() operation?
	// Can be used as utility method for implementations of FlatFile
	protected boolean shouldReload() {
		if (ReloadSettings.AUTOMATICALLY.equals(reloadSettings)) {
			return true;
		} else if (ReloadSettings.INTELLIGENT.equals(reloadSettings)) {
			return FileUtils.hasChanged(file, lastLoaded);
		} else {
			return false;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Misc
	// ----------------------------------------------------------------------------------------------------

	public final FlatFileSection getSection(final String pathPrefix) {
		return new FlatFileSection(this, pathPrefix);
	}

	@Override
	public final int compareTo(final FlatFile flatFile) {
		return file.compareTo(flatFile.file);
	}
}