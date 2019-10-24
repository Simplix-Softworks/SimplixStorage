package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.internal.base.enums.ReloadSetting;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused"})
public class CSVFile extends FlatFile {

	public CSVFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final FileData.Type fileDataType) {
		super(file, FileType.CSV);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (fileDataType != null) {
			setFileDataType(fileDataType);
		} else {
			setFileDataType(FileData.Type.STANDARD);
		}

		reload();
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}
	}


	//added method for later implementation
	@Override
	public void reload() {
		//TODO
	}

	@Override
	public Object get(@NotNull final String key) {
		return null;
	}

	@Override
	public void set(@NotNull final String key, @Nullable final Object value) {
		
	}


	//added method for later implementation
	@Override
	public synchronized void remove(@NotNull final String key) {
		//TODO
	}

	/**
	 * Sets a value to the file if the file doesn't already contain the value
	 * (Not mix up with Bukkit addDefault)
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */
	@Override
	public void setDefault(@NotNull final String key, @Nullable final Object value) {

	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			CSVFile csv = (CSVFile) obj;
			return super.equals(csv.getFlatFileInstance());
		}
	}
}