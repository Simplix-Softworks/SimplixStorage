package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manager Lightning-Type Files
 */
@SuppressWarnings("unused")
public class LightningFile extends FlatFile {

	public LightningFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final DataType dataType) {
		super(file, FileType.LIGHTNING);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (configSetting != null) {
			setConfigSetting(configSetting);
		}
		if (dataType != null) {
			setDataType(dataType);
		}
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}

		this.fileData = new FileData(LightningEditor.readData(this.file, getDataType(), getConfigSetting()));
		this.lastLoaded = System.currentTimeMillis();
	}

	public void reload(@NotNull final ConfigSetting configSetting) {
		setConfigSetting(configSetting);
		reload();
	}

	@Override
	public void reload() {
		try {
			this.fileData.loadData(LightningEditor.readData(this.file, getDataType(), getConfigSetting()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	public Object get(@NotNull final String key, @NotNull final ConfigSetting configSetting) {
		setConfigSetting(configSetting);
		return get(key);
	}

	@Override
	public Object get(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final ConfigSetting configSetting) {
		setConfigSetting(configSetting);
		set(key, value);
	}

	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		if (insert(key, value)) {
			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), getConfigSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	public synchronized void remove(@NotNull final String key, @NotNull final ConfigSetting configSetting) {
		setConfigSetting(configSetting);
		remove(key);
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (fileData.containsKey(finalKey)) {
			fileData.remove(finalKey);

			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), getConfigSetting());
			} catch (IllegalStateException e) {
				System.err.println("Error while writing to '" + getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	protected final LightningFile getLightningFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
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