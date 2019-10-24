package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.internal.base.enums.ReloadSetting;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused"})
public class TomlFile extends FlatFile {

	public TomlFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final FileData.Type fileDataType) {
		super(file, FileType.TOML);
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


	@Override
	public void reload() {
		try {
			fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
		} catch (IOException e) {
			System.err.println("Exception while reading '" + getName() + "'");
			e.printStackTrace();
		}
	}

	@Override
	public Object get(@NotNull final String key) {
		update();
		return fileData.get(key);
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

	/**
	 * Set an object to your file
	 *
	 * @param key   The key your value should be associated with
	 * @param value The value you want to set in your file
	 */
	@SuppressWarnings("Duplicates")
	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		if (insert(key, value)) {
			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Error while writing to '" + file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (fileData.containsKey(finalKey)) {
			fileData.remove(finalKey);

			try {
				com.electronwill.toml.Toml.write(fileData.toMap(), getFile());
			} catch (IOException e) {
				System.err.println("Exception while writing to Toml file '" + getName() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	protected final TomlFile getTomlFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlFile toml = (TomlFile) obj;
			return super.equals(toml.getFlatFileInstance());
		}
	}
}