package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class TomlSection {

	private final TomlFile tomlFile;
	@Getter
	@Setter
	protected String sectionKey;


	public TomlSection(@NotNull final TomlFile tomlFile, @NotNull final String key) {
		this.tomlFile = tomlFile;
		this.sectionKey = key;
	}

	public Object get(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.get(tempKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.tomlFile.set(tempKey, value);
	}

	public synchronized void remove(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.tomlFile.remove(tempKey);
	}

	public boolean hasKey(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.hasKey(tempKey);
	}

	public Set<String> keySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.keySet(tempKey);
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.singleLayerKeySet(tempKey);
	}

	public boolean getBoolean(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getBoolean(tempKey);
	}

	public byte getByte(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getByte(tempKey);
	}

	public List<Byte> getByteList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getByteList(tempKey);
	}

	public double getDouble(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getDouble(tempKey);
	}

	public float getFloat(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getFloat(tempKey);
	}

	public int getInt(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getInt(tempKey);
	}

	public short getShort(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getShort(tempKey);
	}

	public List<Integer> getIntegerList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getIntegerList(tempKey);
	}

	public List<?> getList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getList(tempKey);
	}

	public long getLong(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getLong(tempKey);
	}

	public List<Long> getLongList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getLongList(tempKey);
	}

	public Map getMap(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getMap(tempKey);
	}

	public <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getOrSetDefault(tempKey, value);
	}

	public String getString(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getString(tempKey);
	}

	public List<String> getStringList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.tomlFile.getStringList(tempKey);
	}

	public void setDefault(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.tomlFile.setDefault(tempKey, value);
	}

	protected TomlSection getTomlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlSection tomlSection = (TomlSection) obj;
			return this.tomlFile.equals(tomlSection.tomlFile)
				   && this.sectionKey.equals(tomlSection.sectionKey);
		}
	}
}