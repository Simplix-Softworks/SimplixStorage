package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;


public class LightningFile extends FlatFile {
	private final LightningEditor lightningEditor;

	public LightningFile(final String name, final String path) {
		this(name, path, null, null);
	}

	public LightningFile(final String name, final String path, final InputStream inputStream, final ReloadSettings reloadSettings) {
		super(name, path, FileType.LS);
		lightningEditor = new LightningEditor(file);
		if (create()) {
			if (inputStream != null) {
				FileUtils.writeToFile(this.file, inputStream);
			}
		}

		update();
		if (reloadSettings != null) {
			setReloadSettings(reloadSettings);
		}
	}

	@Override
	public Object get(final String key) {
		reload();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@Override
	public synchronized void set(final String key, final Object value) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		reload();

		String oldData = fileData.toString();
		fileData.insert(finalKey, value);

		if (!oldData.equals(fileData.toString())) {
			lightningEditor.write(fileData);
		}
	}

	@Override
	public boolean contains(String key) {
		return false;
	}

	@Override
	public synchronized void remove(final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		reload();

		fileData.remove(finalKey);

		lightningEditor.write(fileData);
	}

	@Override
	protected void update() {
		try {
			this.fileData = new FileData(lightningEditor.read());
		} catch (IOException e) {
			System.err.println("Error updating LightningFile '" + file.getName() + "'");
			System.err.println("Path: '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningFile lightningFile = (LightningFile) obj;
			return super.equals(lightningFile.getFlatFileInstance());
		}
	}
}