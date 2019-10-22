package de.leonhard.storage.lightningstorage.internal.base;

import de.leonhard.storage.lightningstorage.utils.FileUtils;
import de.leonhard.storage.lightningstorage.utils.basic.FileTypeUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"UnusedReturnValue", "unused"})
@Getter
public abstract class FlatFile implements StorageBase, Comparable<FlatFile> {

	protected File file;
	protected FileData fileData;
	protected FileType fileType;

	@Setter
	private String pathPrefix;
	@Setter
	private ReloadSetting reloadSetting = ReloadSetting.INTELLIGENT;
	@Setter
	private ConfigSetting configSetting = ConfigSetting.SKIP_COMMENTS;
	@Setter
	private FileData.Type fileDataType = FileData.Type.AUTOMATIC;
	private long lastModified;


	public FlatFile(@NotNull final File file, @NotNull final FileType fileType) {
		if (FileTypeUtils.isType(file, fileType)) {
			this.fileType = fileType;
			this.file = file;
		} else {
			throw new IllegalStateException("File '" + file.getName() + "' is not of type '" + fileType + "'");
		}
	}

	public final String getFilePath() {
		return this.file.getAbsolutePath();
	}

	public final String getName() {
		return this.file.getName();
	}

	@Override
	public boolean hasKey(@NotNull final String key) {
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
		return FileUtils.hasChanged(file, lastModified);
	}

	/**
	 * Reread the content of our flat file
	 */
	public abstract void reload();

	@Override
	public Set<String> keySet() {
		update();
		return fileData.keySet();
	}

	@Override
	public Set<String> keySet(@NotNull final String key) {
		update();
		return fileData.keySet(key);
	}

	@Override
	public Set<String> singleLayerKeySet() {
		update();
		return fileData.singleLayerKeySet();
	}

	@Override
	public Set<String> singleLayerKeySet(@NotNull final String key) {
		update();
		return fileData.singleLayerKeySet(key);
	}

	/**
	 * Creates an empty file.
	 *
	 * @return true if file was created.
	 */
	protected final synchronized boolean create() {
		if (this.file.exists()) {
			this.lastModified = System.currentTimeMillis();
			return false;
		} else {
			FileUtils.createFile(this.file);
			this.lastModified = System.currentTimeMillis();
			return true;
		}
	}

	protected final FlatFile getFlatFileInstance() {
		return this;
	}

	protected void replace(@NotNull final CharSequence target, @NotNull final CharSequence replacement) throws IOException {
		final List<String> lines = Files.readAllLines(file.toPath());
		final List<String> result = new ArrayList<>();
		for (String line : lines) {
			result.add(line.replace(target, replacement));
		}
		Files.write(file.toPath(), result);
	}

	@Override
	public synchronized int hashCode() {
		return this.file.hashCode();
	}

	@Override
	public synchronized int compareTo(final FlatFile flatFile) {
		return this.file.compareTo(flatFile.file);
	}

	@Override
	public synchronized String toString() {
		return this.file.getAbsolutePath();
	}

	@Override
	public synchronized boolean equals(@Nullable final Object obj) {
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
				   && this.lastModified == flatFile.lastModified;
		}
	}


	public enum FileType {

		JSON("json"),
		YAML("yml"),
		TOML("toml"),
		CSV("csv"),
		LIGHTNING("ls"),
		DEFAULT("");


		private String extension;

		FileType(@NotNull final String extension) {
			this.extension = extension;
		}

		@Override
		public String toString() {
			return extension;
		}
	}

	public enum ConfigSetting {

		PRESERVE_COMMENTS,
		SKIP_COMMENTS
	}

	public enum ReloadSetting {

		AUTOMATICALLY,
		INTELLIGENT,
		MANUALLY
	}
}