package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Toml extends FlatFile {

	public Toml(Toml toml) {
		super(toml.getFile(), toml.fileType);
		this.fileData = toml.getFileData();
	}

	public Toml(String name, String path) {
		this(name, path, null);
	}

	public Toml(String name, String path, ReloadSettings reloadSettings) {
		super(name, path, FileType.TOML);
		create();
		if (reloadSettings != null) {
			this.reloadSettings = reloadSettings;
		}

		create();
		reRead();
	}

	public Toml(File file) {
		super(file, FileType.TOML);
		create();
		reRead();
	}

	// ----------------------------------------------------------------------------------------------------
	// Abstract methods to implement
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected void reRead() {
		try {
			fileData = new FileData(com.electronwill.toml.Toml.read(getFile()), dataType);
		} catch (IOException ex) {
			System.err.println("Exception while reloading '" + getName() + "'");
			System.err.println("Directory: '" + FileUtils.getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
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