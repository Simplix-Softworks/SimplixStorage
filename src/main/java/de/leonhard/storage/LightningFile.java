package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.lighningfile.LightningEditor;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.InputStream;


/**
 * Class to manager Lightning-Type Files
 */
@SuppressWarnings("unused")
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

		reRead();
		if (reloadSetting != null) {
			this.reloadSettings = reloadSetting;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Abstract methods to implement
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected void reRead() {
		this.fileData = new FileData(lightningEditor.readData());
	}

	@Override
	protected void write(FileData data) {
		lightningEditor.writeData(data.toMap(), configSettings);
	}
}