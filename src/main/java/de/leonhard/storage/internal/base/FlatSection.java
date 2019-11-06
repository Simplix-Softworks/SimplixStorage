package de.leonhard.storage.internal.base;

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


	protected FlatSection(final @NotNull FlatFile flatFile, final @NotNull String sectionKey) {
		this.flatFile = flatFile;
		this.sectionKey = Objects.notNull(sectionKey, "Key must not be null");
	}

	@Override
	public Object get(final @NotNull String key) {
		String tempKey = this.getTempKey(key);

		return this.flatFile.get(tempKey);
	}

	protected String getTempKey(final @NotNull String key) {
		return (this.sectionKey == null || this.sectionKey.isEmpty()) ? Objects.notNull(key, "Key must not be null") : this.sectionKey + "." + Objects.notNull(key, "Key must not be null");
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		this.flatFile.set(this.getTempKey(key), value);
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> map) {
		this.flatFile.setAll(this.sectionKey, map);
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> map) {
		this.flatFile.setAll(this.getTempKey(key), map);
	}

	public synchronized void set(final @Nullable Object value) {
		this.flatFile.set(this.sectionKey, value);
	}

	public synchronized void remove() {
		this.flatFile.remove(this.sectionKey);
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		this.flatFile.remove(this.getTempKey(key));
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> list) {
		this.flatFile.removeAll(this.sectionKey, list);
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> list) {
		this.flatFile.removeAll(this.getTempKey(key), list);
	}

	@Override
	public boolean hasKey(final @NotNull String key) {
		return this.flatFile.hasKey(this.getTempKey(key));
	}

	@Override
	public Set<String> keySet() {
		return this.flatFile.keySet(this.sectionKey);
	}

	@Override
	public Set<String> singleLayerKeySet() {
		return this.flatFile.singleLayerKeySet(this.sectionKey);
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		return this.flatFile.keySet(this.getTempKey(key));
	}

	@Override
	public Set<String> singleLayerKeySet(final @NotNull String key) {
		return this.flatFile.singleLayerKeySet(this.getTempKey(key));
	}

	protected final FlatSection getSectionInstance() {
		return this;
	}

	@Override
	public String toString() {
		return "SectionKey: " + this.sectionKey + ", File: " + this.flatFile.toString();
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
}