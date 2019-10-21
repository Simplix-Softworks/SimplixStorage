package de.leonhard.storage;


import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.Getter;
import lombok.Synchronized;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Getter
public class Toml extends FlatFile {

	public Toml(final String name, final String path) {
		this(name, path, null);
	}

	public Toml(final String name, final String path, final ReloadSettings reloadSettings) {
		super(name, path, FileType.TOML);
		if (reloadSettings != null) {
			this.reloadSettings = reloadSettings;
		}
		update();
	}

	public Toml(final File file) {
		super(file, FileType.TOML);
		create();
		update();
	}

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */

	@Override
	@Synchronized
	public void set(final String key, final Object value) {
		reload();

		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		final String old = fileData.toString();

		fileData.insert(finalKey, value);

		if (old.equals(fileData.toString())) {
			return;
		}

		try {
			com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing to Toml file '" + getName() + "'");
			e.printStackTrace();
		}
	}

	public void write(final Map<String, Object> data) {
		try {
			com.electronwill.toml.Toml.write(data, getFile());
		} catch (IOException e) {
			System.err.println("Exception while writing fileData to file '" + getName() + "'");
			e.printStackTrace();
		}
	}

	@Override
	protected void update() {
		try {
			fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
		} catch (IOException e) {
			System.err.println("Exception while reading '" + getName() + "'");
			e.printStackTrace();
		}
	}

	/**
	 * Reloads the file when needed see {@link ReloadSettings} for deeper
	 * information
	 */
	@Override
	public void reload() {
		if (shouldReload()) {
			update();
		}
	}

	@Override
	public void remove(final String key) {
		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		fileData.remove(finalKey);

		write(fileData.toMap());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			Toml toml = (Toml) obj;
			return this.fileData.equals(toml.fileData)
					&& this.pathPrefix.equals(toml.pathPrefix)
					&& super.equals(toml.getFlatFileInstance());
		}
	}
}