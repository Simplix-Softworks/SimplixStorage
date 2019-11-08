package de.leonhard.storage.internal.data.raw;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.base.interfaces.FileTypeBase;
import de.leonhard.storage.internal.base.interfaces.ReloadBase;
import de.leonhard.storage.internal.data.section.TomlSection;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.utils.LightningFileUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Toml-Type Files
 */
@SuppressWarnings("unused")
public class TomlFile extends FlatFile {

	protected TomlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadBase reloadSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.TOML);

		if (create() && inputStream != null) {
			LightningFileUtils.writeToFile(this.file, inputStream);
		}

		if (dataType != null) {
			setDataType(dataType);
		} else {
			setDataType(DataType.STANDARD);
		}
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}

		try {
			this.fileData = new LocalFileData(com.electronwill.toml.Toml.read(getFile()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	@Override
	public void reload() {
		try {
			fileData.loadData(com.electronwill.toml.Toml.read(getFile()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		update();

		fileData.remove(key);

		try {
			com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing to Toml file '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String key : keys) {
			fileData.remove(key);
		}

		try {
			com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing to Toml file '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String tempKey : keys) {
			fileData.remove(key + "." + tempKey);
		}

		try {
			com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing to Toml file '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public TomlSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	protected final TomlFile getTomlFileInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlFile toml = (TomlFile) obj;
			return super.equals(toml.getFlatFileInstance());
		}
	}


	public enum FileType implements FileTypeBase {

		TOML("toml");


		private final String extension;

		FileType(final @NotNull String extension) {
			this.extension = extension;
		}

		@Override
		public String toLowerCase() {
			return this.extension.toLowerCase();
		}

		@Override
		public String toString() {
			return this.extension;
		}
	}


	private static class LocalSection extends TomlSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull TomlFile tomlFile) {
			super(sectionKey, tomlFile);
		}
	}
}