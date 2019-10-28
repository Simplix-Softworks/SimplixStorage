package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.Valid;
import lombok.Getter;

import java.io.InputStream;


/**
 * Class to manager Lightning-Type Files
 */
@SuppressWarnings("unused")
@Getter
public class LightningFile extends FlatFile {

	private final LightningEditor lightningEditor;
	private ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;

	public LightningFile(String name, String path) {
		this(name, path, null, null, null);
	}

	public LightningFile(String name, String path, InputStream inputStream,
	                     ReloadSettings reloadSetting,
	                     ConfigSettings configSettings) {
		super(name, path, FileType.LS);
		lightningEditor = new LightningEditor(file);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (configSettings != null) {
			this.configSettings = configSettings;
		}

		update();
		if (reloadSetting != null) {
			this.reloadSettings = reloadSetting;
		}
	}

	@Override
	protected void update() {
		this.fileData = new FileData(lightningEditor.readData());
	}

	@Override
	public Object get(String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@Override
	public synchronized void set(String key, Object value) {
		Valid.notNull(key, "Key must not be null");
		fileData.insert(key, value);
		try {
			lightningEditor.writeData(this.fileData.toMap(), configSettings);
		} catch (IllegalStateException | IllegalArgumentException e) {
			System.err.println("Error while writing to '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void remove(String key) {
		Valid.notNull(key, "Key must not be null");
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (!fileData.containsKey(finalKey)) {
			return;
		}
		fileData.remove(finalKey);

		try {
			lightningEditor.writeData(this.fileData.toMap(), getConfigSettings());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	protected final LightningFile getLightningFileInstance() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
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