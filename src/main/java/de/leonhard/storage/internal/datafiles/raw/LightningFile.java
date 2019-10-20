package de.leonhard.storage.internal.datafiles.raw;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.base.exceptions.InvalidSettingException;
import de.leonhard.storage.internal.base.exceptions.LightningFileReadException;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.enums.ConfigSetting;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.Getter;
import lombok.Setter;


@SuppressWarnings({"unused"})
@Getter
public class LightningFile extends FlatFile {

	private final LightningEditor lightningEditor;
	@Setter
	private ConfigSetting configSetting = ConfigSetting.SKIP_COMMENTS;

	public LightningFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting) throws InvalidFileTypeException {
		if (FileTypeUtils.isType(file, FileType.LIGHTNING)) {
			if (create(file)) {
				if (inputStream != null) {
					FileUtils.writeToFile(this.file, inputStream);
				}
			}

			this.lightningEditor = new LightningEditor(this.file);
			reload();
			if (reloadSetting != null) {
				setReloadSetting(reloadSetting);
			}
		} else {
			throw new InvalidFileTypeException("The given file if of no valid filetype.");
		}
	}

	@Override
	public void reload() {
		try {
			this.fileData = new FileData(this.lightningEditor.read(this.configSetting));
		} catch (IOException | LightningFileReadException | InvalidSettingException e) {
			System.err.println("Error while reading '" + file.getName() + "'");
			e.printStackTrace();
		}
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
				this.lightningEditor.write(this.fileData, this.configSetting);
			} catch (InvalidSettingException e) {
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
			this.lightningEditor.write(this.fileData, this.configSetting);
		} catch (InvalidSettingException e) {
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