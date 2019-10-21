package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.utils.FileTypeUtils;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused"})
public class TomlFile extends FlatFile {

	public TomlFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		if (FileTypeUtils.isType(file, FileType.TOML)) {
			if (create(file)) {
				if (inputStream != null) {
					FileUtils.writeToFile(this.file, inputStream);
				}
			}

			if (configSetting != null) {
				setConfigSetting(configSetting);
			}
			if (fileDataType != null) {
				setFileDataType(fileDataType);
			} else {
				setFileDataType(FileData.Type.STANDARD);
			}

			reload();
			if (reloadSetting != null) {
				setReloadSetting(reloadSetting);
			}
		} else {
			throw new IllegalStateException("The given file is no TOML-File");
		}
	}

	@Override
	public void reload() {
		try {
			fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
		} catch (IOException e) {
			System.err.println("Exception while reading '" + getName() + "'");
			e.printStackTrace();
		}
	}

	@Override
	public Object get(final String key) {
		update();
		return fileData.get(key);
	}

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@SuppressWarnings("Duplicates")
	@Override
	public synchronized void set(final String key, final Object value) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		String oldData = fileData.toString();
		fileData.insert(finalKey, value);

		if (!oldData.equals(fileData.toString())) {
			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Exception while writing to Toml file '" + getName() + "'");
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void remove(final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		fileData.remove(finalKey);

		try {
			com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing to Toml file '" + getName() + "'");
			e.printStackTrace();
		}
	}

	protected final TomlFile getTomlFileInstance() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
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