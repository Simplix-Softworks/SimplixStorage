package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class JsonSection {

	private final JsonFile jsonFile;
	@Getter
	@Setter
	protected String sectionKey;

	public JsonSection(@NotNull final JsonFile jsonFile, @NotNull final String key) {
		this.jsonFile = jsonFile;
		this.sectionKey = key;
	}

	public Object get(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.get(tempKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.jsonFile.set(tempKey, value);
	}

	public synchronized void remove(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.jsonFile.remove(tempKey);
	}

	public boolean hasKey(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.hasKey(tempKey);
	}

	public Set<String> keySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.keySet(tempKey);
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.singleLayerKeySet(tempKey);
	}

	public boolean getBoolean(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getBoolean(tempKey);
	}

	public byte getByte(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getByte(tempKey);
	}

	public List<Byte> getByteList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getByteList(tempKey);
	}

	public double getDouble(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getDouble(tempKey);
	}

	public float getFloat(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getFloat(tempKey);
	}

	public int getInt(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getInt(tempKey);
	}

	public short getShort(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getShort(tempKey);
	}

	public List<Integer> getIntegerList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getIntegerList(tempKey);
	}

	public List<?> getList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getList(tempKey);
	}

	public long getLong(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getLong(tempKey);
	}

	public List<Long> getLongList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getLongList(tempKey);
	}

	public Map getMap(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getMap(tempKey);
	}

	public <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getOrSetDefault(tempKey, value);
	}

	public String getString(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getString(tempKey);
	}

	public List<String> getStringList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.jsonFile.getStringList(tempKey);
	}

	public void setDefault(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.jsonFile.setDefault(tempKey, value);
	}

	protected JsonSection getJsonSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonSection jsonSection = (JsonSection) obj;
			return this.jsonFile.equals(jsonSection.jsonFile)
				   && this.sectionKey.equals(jsonSection.sectionKey);
		}
	}
}