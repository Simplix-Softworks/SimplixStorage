package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.ReloadSetting;
import de.leonhard.storage.internal.utils.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage CSV-Type Files
 */
@SuppressWarnings("unused")
public class CSVFile extends FlatFile {

	public CSVFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final DataType dataType) {
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
	public Object get(@NotNull final String key) {
		return null;
	}

	@Override
	public boolean getBoolean(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getBoolean(finalKey);
	}

	@Override
	public byte getByte(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getByte(finalKey);
	}

	@Override
	public List<Byte> getByteList(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getByteList(finalKey);
	}

	@Override
	public double getDouble(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getDouble(finalKey);
	}

	@Override
	public float getFloat(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getFloat(finalKey);
	}

	@Override
	public int getInt(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getInt(finalKey);
	}

	@Override
	public List<Integer> getIntegerList(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getIntegerList(finalKey);
	}

	@Override
	public List<?> getList(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getList(finalKey);
	}

	@Override
	public long getLong(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getLong(finalKey);
	}

	@Override
	public List<Long> getLongList(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getLongList(finalKey);
	}

	@Override
	public Map getMap(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getMap(finalKey);
	}

	@Override
	public String getString(@NotNull final String key) {
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return super.getString(finalKey);
	}

	@Override
	public void set(@NotNull final String key, @Nullable final Object value) {

	}


	//added method for later implementation
	@Override
	public synchronized void remove(@NotNull final String key) {
		//TODO
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