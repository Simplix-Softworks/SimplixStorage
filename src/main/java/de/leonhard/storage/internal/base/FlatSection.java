package de.leonhard.storage.internal.base;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public abstract class FlatSection implements StorageBase {

	private final FlatFile flatFile;
	@Getter
	@Setter
	protected String sectionKey;


	public FlatSection(@NotNull final FlatFile flatFile, @NotNull final String sectionKey) {
		this.flatFile = flatFile;
		this.sectionKey = sectionKey;
	}

	public Object get(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.get(tempKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.flatFile.set(tempKey, value);
	}

	public synchronized void remove(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.flatFile.remove(tempKey);
	}

	public boolean hasKey(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.hasKey(tempKey);
	}

	public Set<String> keySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.keySet(tempKey);
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.singleLayerKeySet(tempKey);
	}

	public boolean getBoolean(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getBoolean(tempKey);
	}

	public byte getByte(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getByte(tempKey);
	}

	public List<Byte> getByteList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getByteList(tempKey);
	}

	public double getDouble(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getDouble(tempKey);
	}

	public float getFloat(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getFloat(tempKey);
	}

	public int getInt(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getInt(tempKey);
	}

	public short getShort(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getShort(tempKey);
	}

	public List<Integer> getIntegerList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getIntegerList(tempKey);
	}

	public List<?> getList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getList(tempKey);
	}

	public long getLong(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getLong(tempKey);
	}

	public List<Long> getLongList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getLongList(tempKey);
	}

	public Map getMap(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getMap(tempKey);
	}

	public <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getOrSetDefault(tempKey, value);
	}

	public String getString(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getString(tempKey);
	}

	public List<String> getStringList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.getStringList(tempKey);
	}

	public void setDefault(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.flatFile.setDefault(tempKey, value);
	}

	protected FlatSection getSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FlatSection flatSection = (FlatSection) obj;
			return this.flatFile.equals(flatSection.flatFile)
				   && this.sectionKey.equals(flatSection.sectionKey);
		}
	}
}