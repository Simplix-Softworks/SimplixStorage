package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused"})
public class CSVFile extends FlatFile {

	public CSVFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		super(file, FileType.CSV);
		if (create()) {
			if (inputStream != null) {
				FileUtils.writeToFile(this.file, inputStream);
			}
		}

		if (configSetting != null) {
			setConfigSetting(configSetting);
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

	//added method for later implementation
	@Override
	public Set<String> keySet() {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> keySet(final String key) {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> singleLayerKeySet() {
		return null;
	}

	//added method for later implementation
	@Override
	public Set<String> singleLayerKeySet(final String key) {
		return null;
	}

	/**
	 * Get a boolean from a file
	 *
	 * @param key Path to boolean in file
	 * @return Boolean from file
	 */
	@Override
	public boolean getBoolean(final String key) {
		return false;
	}

	@Override
	public Object get(final String key) {
		return null;
	}

	/**
	 * Get a byte from a file
	 *
	 * @param key Path to byte in file
	 * @return Byte from file
	 */
	@Override
	public byte getByte(final String key) {
		return 0;
	}

	/**
	 * Get a Byte-List from a file
	 *
	 * @param key Path to Byte-List from file
	 * @return Byte-List
	 */
	@Override
	public List<Byte> getByteList(final String key) {
		return null;
	}

	/**
	 * Get a double from a file
	 *
	 * @param key Path to double in file
	 * @return Double from file
	 */
	@Override
	public double getDouble(final String key) {
		return 0;
	}

	/**
	 * Get a float from a file
	 *
	 * @param key Path to float in file
	 * @return Float from file
	 */
	@Override
	public float getFloat(final String key) {
		return 0;
	}

	/**
	 * Gets a int from a file
	 *
	 * @param key Path to int in file
	 * @return Int from file
	 */
	@Override
	public int getInt(final String key) {
		return 0;
	}

	/**
	 * Get a IntegerList from a file
	 *
	 * @param key Path to Integer-List in file
	 * @return Integer-List
	 */
	@Override
	public List<Integer> getIntegerList(final String key) {
		return null;
	}

	/**
	 * Get a List from a file
	 *
	 * @param key Path to StringList in file
	 * @return List
	 */
	@Override
	public List<?> getList(final String key) {
		return null;
	}

	/**
	 * Gets a long from a file by key
	 *
	 * @param key Path to long in file
	 * @return String from file
	 */
	@Override
	public long getLong(final String key) {
		return 0;
	}

	/**
	 * Get a Long-List from a file
	 *
	 * @param key Path to Long-List in file
	 * @return Long-List
	 */
	@Override
	public List<Long> getLongList(final String key) {
		return null;
	}

	/**
	 * Gets a Map
	 *
	 * @param key Path to Map-List in file
	 * @return Map
	 */
	@Override
	public Map getMap(final String key) {
		return null;
	}

	@Override
	public <T> T getOrSetDefault(final String path, final T def) {
		return null;
	}

	/**
	 * Set a object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@Override
	public synchronized void set(final String key, final Object value) {
		//TODO
	}

	/**
	 * Get a String from a file
	 *
	 * @param key Path to String in file
	 * @return Returns the value
	 */
	@Override
	public String getString(final String key) {
		return null;
	}

	/**
	 * Get String List
	 *
	 * @param key Path to String List in file
	 * @return List
	 */
	@Override
	public List<String> getStringList(final String key) {
		return null;
	}

	//added method for later implementation
	@Override
	public synchronized void remove(final String key) {
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
	public void setDefault(final String key, final Object value) {
		//TODO
	}

	@Override
	public boolean equals(final Object obj) {
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