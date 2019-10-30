package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.enums.Comment;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class YamlSection {

	private final YamlFile yamlFile;

	@Getter
	@Setter
	private String sectionKey;

	public YamlSection(@NotNull final YamlFile yamlFile, @NotNull final String key) {
		this.yamlFile = yamlFile;
		this.sectionKey = key;
	}

	public Object get(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.get(tempKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.yamlFile.set(tempKey, value, commentSetting);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.yamlFile.set(tempKey, value);
	}

	public synchronized void remove(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.yamlFile.remove(tempKey);
	}

	public boolean hasKey(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.hasKey(tempKey);
	}

	public Set<String> keySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.keySet(tempKey);
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.singleLayerKeySet(tempKey);
	}

	public boolean getBoolean(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getBoolean(tempKey);
	}

	public byte getByte(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getByte(tempKey);
	}

	public List<Byte> getByteList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getByteList(tempKey);
	}

	public double getDouble(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getDouble(tempKey);
	}

	public float getFloat(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getFloat(tempKey);
	}

	public int getInt(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getInt(tempKey);
	}

	public short getShort(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getShort(tempKey);
	}

	public List<Integer> getIntegerList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getIntegerList(tempKey);
	}

	public List<?> getList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getList(tempKey);
	}

	public long getLong(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getLong(tempKey);
	}

	public List<Long> getLongList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getLongList(tempKey);
	}

	public Map getMap(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getMap(tempKey);
	}

	public <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getOrSetDefault(tempKey, value);
	}

	public String getString(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getString(tempKey);
	}

	public List<String> getStringList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.yamlFile.getStringList(tempKey);
	}

	public void setDefault(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.yamlFile.setDefault(tempKey, value);
	}

	protected YamlSection getYamlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlSection yamlSection = (YamlSection) obj;
			return this.sectionKey.equals(yamlSection.sectionKey)
				   && this.yamlFile.equals(yamlSection.yamlFile);
		}
	}
}