package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manager Toml-Type Files
 */
@SuppressWarnings("unused")
public class TomlFile extends FlatFile {

	public TomlFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final DataType dataType) {
		super(file, FileType.TOML);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (dataType != null) {
			setDataType(dataType);
		} else {
			setDataType(DataType.STANDARD);
		}

		reload();
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}
	}


	@Override
	public void reload() {
		try {
			fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public Object get(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(key);
	}

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		if (insert(key, value)) {
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
	public synchronized void remove(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (fileData.containsKey(finalKey)) {
			fileData.remove(finalKey);

			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Exception while writing to Toml file '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	protected final TomlFile getTomlFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlFile toml = (TomlFile) obj;
			return super.equals(toml.getFlatFileInstance());
		}
	}
}