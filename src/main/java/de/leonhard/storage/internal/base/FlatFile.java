package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.FileTypeUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Set;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic foundation for the Data Classes
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
@Getter
public abstract class FlatFile implements StorageBase, Comparable<FlatFile> {

	protected File file;
	protected FileData fileData;
	protected long lastLoaded;

	@Setter
	private String pathPrefix;
	@Setter
	private ReloadSetting reloadSetting = ReloadSetting.INTELLIGENT;
	@Setter
	private ConfigSetting configSetting = ConfigSetting.SKIP_COMMENTS;
	@Setter
	private DataType dataType = DataType.AUTOMATIC;
	private FileType fileType;


	public FlatFile(@NotNull final File file, @NotNull final FileType fileType) {
		if (FileTypeUtils.isType(file, fileType)) {
			this.fileType = fileType;
			this.file = file;
		} else {
			throw new IllegalStateException("File '" + file.getAbsolutePath() + "' is not of type '" + fileType + "'");
		}
	}


	public final String getAbsolutePath() {
		return this.file.getAbsolutePath();
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

	public final synchronized void setFileContentFromStream(@Nullable final InputStream inputStream) {
		if (inputStream == null) {
			clearFile();
		} else {
			FileUtils.writeToFile(this.file, inputStream);
			reload();
		}
	}

	public final synchronized void clearFile() {
		try {
			@Cleanup BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
			writer.write("");
			reload();
		} catch (IOException e) {
			System.err.println("Could not clear '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Reread the content of our flat file
	 */
	public abstract void reload();

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

	public final synchronized void setFileContentFromFile(@Nullable final File file) {
		if (file == null) {
			clearFile();
		} else {
			FileUtils.writeToFile(this.file, FileUtils.createNewInputStream(file));
			reload();
		}
	}

	public final synchronized void setFileContentFromResource(@Nullable final String resource) {
		if (resource == null) {
			clearFile();
		} else {
			FileUtils.writeToFile(this.file, FileUtils.createNewInputStream(resource));
			reload();
		}
	}

	@Override
	public boolean hasKey(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		String tempKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		update();
		return fileData.containsKey(tempKey);
	}

	/**
	 * Checks if the File needs to be reloaded and does so if true.
	 */
	protected final void update() {
		if (shouldReload()) {
			reload();
		}
	}

	protected boolean shouldReload() {
		switch (this.reloadSetting) {
			case AUTOMATICALLY:
				return true;
			case INTELLIGENT:
				return hasChanged();
			case MANUALLY:
				return false;
			default:
				throw new IllegalArgumentException("Illegal ReloadSetting");
		}
	}

	/**
	 * Returns if the File has changed since the last check.
	 *
	 * @return true if it has changed.
	 */
	public final boolean hasChanged() {
		return FileUtils.hasChanged(file, lastLoaded);
	}

	public Set<String> keySet() {
		update();
		return fileData.keySet();
	}

	public Set<String> keySet(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		return fileData.keySet(key);
	}

	public Set<String> singleLayerKeySet() {
		update();
		return fileData.singleLayerKeySet();
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		return fileData.singleLayerKeySet(key);
	}

	/**
	 * Insert a key-value-pair to the FileData.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to @param key.
	 * @return true if the Data contained by FileData contained after adding the key-value-pair.
	 */
	protected final boolean insert(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		update();

		final String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;

		String tempData = this.fileData.toString();
		this.fileData.insert(finalKey, value);
		return !this.fileData.toString().equals(tempData);
	}

	/**
	 * Creates an empty file.
	 *
	 * @return true if file was created.
	 */
	protected final synchronized boolean create() {
		if (this.file.exists()) {
			this.lastLoaded = System.currentTimeMillis();
			return false;
		} else {
			FileUtils.createFile(this.file);
			this.lastLoaded = System.currentTimeMillis();
			return true;
		}
	}

	protected final FlatFile getFlatFileInstance() {
		return this;
	}

	/**
	 * replaces a give CharSequence with another in the File.
	 *
	 * @param target      the CharSequence to be replaced.
	 * @param replacement the Replacement Sequence.
	 */
	protected synchronized void replace(@NotNull final CharSequence target, @NotNull final CharSequence replacement) throws IOException {
		Valid.notNull(target, "Target must not be null");
		Valid.notNull(replacement, "Replacement must not be null");

		final Iterator lines = Files.readAllLines(this.file.toPath()).iterator();
		PrintWriter writer = new PrintWriter(this.file);
		writer.print(lines.next());
		//noinspection unchecked
		lines.forEachRemaining(line -> {
			writer.println();
			writer.print(line);
		});
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

	@Override
	public int compareTo(final FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}

	@Override
	public String toString() {
		return this.file.getAbsolutePath();
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FlatFile flatFile = (FlatFile) obj;
			return this.pathPrefix.equals(flatFile.pathPrefix)
				   && reloadSetting == flatFile.reloadSetting
				   && this.fileData.equals(flatFile.fileData)
				   && this.file.equals(flatFile.file)
				   && fileType == flatFile.fileType
				   && this.lastLoaded == flatFile.lastLoaded;
		}
	}


	public enum FileType {

		JSON("json"),
		YAML("yml"),
		TOML("toml"),
		CSV("csv"),
		LIGHTNING("ls"),
		DEFAULT("");


		private final String extension;

		FileType(@NotNull final String extension) {
			this.extension = extension;
		}

		@Override
		public String toString() {
			return extension;
		}
	}
}