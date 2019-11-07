package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.data.FileData;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


/**
 * Basic foundation for the Data Classes
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
@Getter
public abstract class FlatFile implements StorageBase, Comparable<FlatFile> {

	protected final File file;
	private final FileTypeBase fileType;
	protected FileData fileData;
	protected long lastLoaded;
	@Setter
	private Reload reloadSetting = Reload.INTELLIGENT;
	@Setter
	private DataType dataType = DataType.AUTOMATIC;


	protected FlatFile(final @NotNull File file, final @NotNull FileTypeBase fileType) {
		if (fileType.isTypeOf(file)) {
			this.fileType = fileType;
			this.file = file;
			this.reloadSetting.setFlatFile(this);
		} else {
			throw new IllegalStateException("File '" + file.getAbsolutePath() + "' is not of type '" + fileType + "'");
		}
	}

	public final String getPath() {
		return this.file.getPath();
	}

	public final String getCanonicalPath() {
		try {
			return this.file.getCanonicalPath();
		} catch (IOException e) {
			System.err.println("Could not get Canonical Path of '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	public final String getName() {
		return this.file.getName();
	}

	/**
	 * Set the Contents of the File from a given InputStream
	 */
	public final synchronized void setFileContentFromStream(final @Nullable InputStream inputStream) {
		if (inputStream == null) {
			this.clearFile();
		} else {
			FileUtils.writeToFile(this.file, inputStream);
			this.reload();
		}
	}

	/**
	 * Delete all Contents of the File
	 */
	public final synchronized void clearFile() {
		try {
			@Cleanup BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
			writer.write("");
			this.reload();
		} catch (IOException e) {
			System.err.println("Could not clear '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Just delete the File
	 */
	public final synchronized void deleteFile() {
		if (!this.file.delete()) {
			System.err.println("Could not delete '" + this.file.getAbsolutePath() + "'");
			throw new IllegalStateException();
		}
	}

	/**
	 * Clears the contents of the internal FileData.
	 * To get any data, you simply need to reload.
	 */
	public final synchronized void clearData() {
		this.fileData.clear();
	}

	/**
	 * Set the Contents of the File from a given File
	 */
	public final synchronized void setFileContentFromFile(final @Nullable File file) {
		if (file == null) {
			this.clearFile();
		} else {
			FileUtils.writeToFile(this.file, FileUtils.createNewInputStream(file));
			this.reload();
		}
	}

	/**
	 * Set the Contents of the File from a given Resource
	 */
	public final synchronized void setFileContentFromResource(final @Nullable String resource) {
		if (resource == null) {
			this.clearFile();
		} else {
			FileUtils.writeToFile(this.file, FileUtils.createNewInputStream(resource));
			this.reload();
		}
	}

	public final String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	/**
	 * Returns if the File has changed since the last check.
	 *
	 * @return true if it has changed.
	 */
	public final boolean hasChanged() {
		return FileUtils.hasChanged(this.file, this.lastLoaded);
	}

	/**
	 * Reread the content of our flat file
	 */
	public abstract void reload();

	@Override
	public boolean hasKey(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		return fileData.containsKey(key);
	}

	@Override
	public Set<String> keySet() {
		this.update();
		return this.fileData.keySet();
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		return this.fileData.keySet(key);
	}

	@Override
	public Set<String> blockKeySet() {
		this.update();
		return this.fileData.blockKeySet();
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		return this.fileData.blockKeySet(key);
	}

	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	public synchronized void replaceInFile(final @NotNull CharSequence target, final @NotNull CharSequence replacement) throws IOException {
		Objects.checkNull(target, "Target must not be null");
		Objects.checkNull(replacement, "Replacement must not be null");

		final Iterator lines = Files.readAllLines(this.file.toPath()).iterator();
		PrintWriter writer = new PrintWriter(this.file);
		writer.print(((String) lines.next()).replace(target, replacement));
		//noinspection unchecked
		lines.forEachRemaining(line -> {
			writer.println();
			writer.print(((String) line).replace(target, replacement));
		});
		this.reload();
	}

	@Override
	public Map<String, Object> getAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "KeyList must not be null");
		this.update();

		Map<String, Object> tempMap = new HashMap<>();
		for (String key : keys) {
			tempMap.put(key, this.fileData.get(key));
		}
		return tempMap;
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(keys, "KeyList must not be null");
		this.update();

		Map<String, Object> tempMap = new HashMap<>();
		for (String tempKey : keys) {
			tempMap.put(key, this.fileData.get(key + "." + tempKey));
		}
		return tempMap;
	}

	/**
	 * Checks if the File needs to be reloaded and does so if true.
	 */
	protected final void update() {
		if (this.shouldReload()) {
			this.reload();
		}
	}

	/**
	 * Insert a key-value-pair to the FileData.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to @param key.
	 * @return true if the Data contained by FileData contained after adding the key-value-pair.
	 */
	protected final boolean insert(final @NotNull String key, final @Nullable Object value) {
		Objects.checkNull(key, "Key must not be null");
		this.update();

		String tempData = this.fileData.toString();
		this.fileData.insert(key, value);
		return !this.fileData.toString().equals(tempData);
	}

	protected final boolean insertAll(final @NotNull Map<String, Object> map) {
		Objects.checkNull(map, "Map must not be null");
		this.update();

		String tempData = this.fileData.toString();
		for (String key : map.keySet()) {
			this.fileData.insert(key, map.get(key));
		}
		return !this.fileData.toString().equals(tempData);
	}

	protected final boolean insertAll(final @NotNull String key, final @NotNull Map<String, Object> map) {
		Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(map, "Map must not be null");
		this.update();

		String tempData = this.fileData.toString();
		for (String tempKey : map.keySet()) {
			this.fileData.insert(key + "." + tempKey, map.get(tempKey));
		}
		return !this.fileData.toString().equals(tempData);
	}

	/**
	 * Creates an empty file.
	 *
	 * @return true if File was created.
	 */
	protected final synchronized boolean create() {
		if (this.file.exists()) {
			return false;
		} else {
			FileUtils.createFile(this.file);
			return true;
		}
	}

	protected final FlatFile getFlatFileInstance() {
		return this;
	}

	protected boolean shouldReload() {
		switch (this.reloadSetting) {
			case AUTOMATICALLY:
				return true;
			case INTELLIGENT:
				return this.hasChanged();
			case MANUALLY:
				return false;
			default:
				throw new IllegalArgumentException("Illegal ReloadSetting in '" + this.getAbsolutePath() + "'");
		}
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FlatFile flatFile = (FlatFile) obj;
			return reloadSetting == flatFile.reloadSetting
				   && this.fileData.equals(flatFile.fileData)
				   && this.file.equals(flatFile.file)
				   && fileType == flatFile.fileType
				   && this.lastLoaded == flatFile.lastLoaded;
		}
	}

	@Override
	public int compareTo(final FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}

	@Override
	public String toString() {
		return this.file.getAbsolutePath();
	}

	protected static class LocalFileData extends FileData {

		public LocalFileData(final @Nullable Map<String, Object> map) {
			super(map);
		}

		public LocalFileData(final @NotNull JSONObject jsonObject) {
			super(jsonObject);
		}
	}
}