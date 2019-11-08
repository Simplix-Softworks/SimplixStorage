package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.base.interfaces.StorageBase;
import de.leonhard.storage.internal.utils.basic.Objects;
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


	protected FlatSection(final @NotNull String sectionKey, final @NotNull FlatFile flatFile) {
		this.sectionKey = Objects.notNull(sectionKey, "Key must not be null");
		this.flatFile = flatFile;
	}

	@Override
	public Object get(final @NotNull String key) {
		return this.flatFile.get(this.getSectionKey(key));
	}

	@Override
	public Map<String, Object> getAll(final @NotNull List<String> keys) {
		return this.flatFile.getAll(keys);
	}

	@Override
	public Map<String, Object> getAll(final @NotNull String key, final @NotNull List<String> keys) {
		return this.flatFile.getAll(this.getSectionKey(key), keys);
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.getSectionKey(key), value);
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.sectionKey, dataMap);
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		this.flatFile.setAll(this.getSectionKey(key), dataMap);
	}

	public synchronized void set(final @Nullable Object value) {
		this.flatFile.set(this.sectionKey, value);
	}

	public synchronized void remove() {
		this.flatFile.remove(this.sectionKey);
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		this.flatFile.remove(this.getSectionKey(key));
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.sectionKey, keys);
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		this.flatFile.removeAll(this.getSectionKey(key), keys);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.getSectionKey(key));
	}

	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(this.sectionKey);
	}

	@Override
	public Set<String> blockKeySet() {
		return this.flatFile.blockKeySet(this.sectionKey);
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		return this.flatFile.keySet(this.getSectionKey(key));
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		return this.flatFile.blockKeySet(this.getSectionKey(key));
	}

	protected final FlatSection getSectionInstance() {
		return this;
	}

	protected String getSectionKey(final @NotNull String key) {
		return (this.sectionKey == null || this.sectionKey.isEmpty()) ? Objects.notNull(key, "Key must not be null") : this.sectionKey + "." + Objects.notNull(key, "Key must not be null");
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
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

	@Override
	public String toString() {
		return "SectionKey: " + this.sectionKey + ", File: " + this.flatFile.toString();
	}
}