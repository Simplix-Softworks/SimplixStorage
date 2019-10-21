package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import de.leonhard.storage.lightningstorage.editor.LightningFileEditor;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.utils.FileTypeUtils;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningFile extends FlatFile {

	protected final LightningFileEditor lightningFileEditor;

	public LightningFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		if (FileTypeUtils.isType(file, FileType.LIGHTNING)) {
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
			}

			this.lightningFileEditor = new LightningFileEditor(this.file, getConfigSetting(), getFileDataType());
			reload();
			if (reloadSetting != null) {
				setReloadSetting(reloadSetting);
			}
		} else {
			throw new IllegalStateException("The given file is no Lightning-File");
		}
	}

	@Override
	public void reload() {
		try {
			this.fileData = new FileData(this.lightningFileEditor.readData());
		} catch (IOException e) {
			System.err.println("Error while reading '" + file.getName() + "'");
			e.printStackTrace();
		}
	}

	@Override
	public void setConfigSetting(final ConfigSetting configSetting) {
		super.setConfigSetting(configSetting);
	}

	@Override
	public Object get(final String key) {
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public synchronized void set(final String key, final Object value) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		String oldData = fileData.toString();
		fileData.insert(finalKey, value);

		if (!oldData.equals(fileData.toString())) {
			try {
				this.lightningFileEditor.writeData(this.fileData);
			} catch (IllegalStateException e) {
				System.err.println("Error while writing to '" + file.getName() + "'");
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
			this.lightningFileEditor.writeData(this.fileData);
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + file.getName() + "'");
			e.printStackTrace();
		}
	}

	protected final LightningFile getLightningFileInstance() {
		return this;
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