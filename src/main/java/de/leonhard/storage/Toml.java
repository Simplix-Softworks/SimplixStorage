package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Getter
public class Toml extends FlatFile {

	public Toml(Toml toml) {
		super(toml.getFile());
		this.fileData = toml.getFileData();
	}

	public Toml(String name, String path) {
		this(name, path, null);
	}

	public Toml(String name, String path, InputStream inputStream) {
		this(name, path, inputStream, null);
	}

	public Toml(String name, String path, InputStream inputStream, ReloadSettings reloadSettings) {
		super(name, path, FileType.TOML);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(file, inputStream);
		}

		if (reloadSettings != null) {
			this.reloadSettings = reloadSettings;
		}

		forceReload();
	}

	public Toml(File file) {
		super(file);
		create();
		forceReload();
	}

	// ----------------------------------------------------------------------------------------------------
	// Abstract methods to implement
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected Map<String, Object> readToMap() throws IOException {
		return com.electronwill.toml.Toml.read(getFile());
	}

	@Override
	protected void write(FileData data) {
		try {
			com.electronwill.toml.Toml.write(data.toMap(), getFile());
		} catch (IOException ex) {
			System.err.println("Exception while writing fileData to file '" + getName() + "'");
			System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
	}
}