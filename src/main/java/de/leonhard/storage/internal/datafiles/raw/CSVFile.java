package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage CSV-Type Files
 */
@SuppressWarnings("unused")
public class CSVFile extends FlatFile {

	public CSVFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final Reload reloadSetting, @Nullable final DataType dataType) {
		super(file, FileType.CSV);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (dataType != null) {
			setDataType(dataType);
		} else {
			setDataType(DataType.STANDARD);
		}
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}
	}


	//added method for later implementation
	@Override
	public void reload() {
		//TODO
		this.lastLoaded = System.currentTimeMillis();
	}

	@Override
	public Object get(final @NotNull String key) {
		return null;
	}

	@Override
	public void set(final @NotNull String key, final @Nullable Object value) {

	}

	@Override
	public void remove(final @NotNull String key) {

	}

	protected final CSVFile getCSVFileInstance() {
		return this;
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