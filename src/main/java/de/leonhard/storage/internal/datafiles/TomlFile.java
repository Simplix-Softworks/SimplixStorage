package de.leonhard.storage.internal.datafiles;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@SuppressWarnings({"unused"})
public class TomlFile extends FlatFile {

	protected TomlFile(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		if (FileTypeUtils.isType(file, FileType.TOML)) {
			if (create(file)) {
				if (inputStream != null) {
					FileUtils.writeToFile(this.file, inputStream);
				}
			}

			update();
			if (reloadSettings != null) {
				setReloadSettings(reloadSettings);
			}
		} else {
			throw new InvalidFileTypeException("The given file if of no valid filetype.");
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

	protected final TomlFile getTomlInstance() {
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