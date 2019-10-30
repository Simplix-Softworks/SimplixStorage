package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.enums.Comment;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningSection {

	private final LightningFile lightningFile;
	@Getter
	@Setter
	private String sectionKey;

	public LightningSection(@NotNull final LightningFile lightningFile, @NotNull final String key) {
		this.lightningFile = lightningFile;
		this.sectionKey = key;
	}

	public Object get(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.get(tempKey);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.set(tempKey, value, commentSetting);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.set(tempKey, value);
	}

	public synchronized void remove(@NotNull final String key, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.remove(tempKey, commentSetting);
	}

	public synchronized void remove(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.remove(tempKey);
	}

	public boolean hasKey(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.hasKey(tempKey);
	}

	public Set<String> keySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.keySet(tempKey);
	}

	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.singleLayerKeySet(tempKey);
	}

	public boolean getBoolean(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getBoolean(tempKey);
	}

	public byte getByte(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getByte(tempKey);
	}

	public List<Byte> getByteList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getByteList(tempKey);
	}

	public double getDouble(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getDouble(tempKey);
	}

	public float getFloat(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getFloat(tempKey);
	}

	public int getInt(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getInt(tempKey);
	}

	public short getShort(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getShort(tempKey);
	}

	public List<Integer> getIntegerList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getIntegerList(tempKey);
	}

	public List<?> getList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getList(tempKey);
	}

	public long getLong(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getLong(tempKey);
	}

	public List<Long> getLongList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getLongList(tempKey);
	}

	public Map getMap(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getMap(tempKey);
	}

	public <T> T getOrSetDefault(@NotNull final String key, @NotNull final T value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getOrSetDefault(tempKey, value);
	}

	public String getString(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getString(tempKey);
	}

	public List<String> getStringList(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningFile.getStringList(tempKey);
	}

	public void setDefault(@NotNull final String key, @Nullable final Object value) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.setDefault(tempKey, value);
	}

	protected LightningSection getLightningSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningSection lightningSection = (LightningSection) obj;
			return this.sectionKey.equals(lightningSection.sectionKey)
				   && this.lightningFile.equals(lightningSection.lightningFile);
		}
	}
}