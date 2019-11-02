package de.leonhard.storage.internal.base;

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

	@Override
	public Object get(@NotNull final String key) {
		String tempKey = this.getTempKey(key);

		return this.flatFile.get(tempKey);
	}

	protected String getTempKey(final String key) {
		return (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;
	}

	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		String tempKey = this.getTempKey(key);

		this.flatFile.set(tempKey, value);
	}

	public synchronized void set(@Nullable final Object value) {
		this.flatFile.set(this.sectionKey, value);
	}

	public synchronized void remove() {
		this.flatFile.remove(this.sectionKey);
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		String tempKey = this.getTempKey(key);

		this.flatFile.remove(tempKey);
	}

	@Override
	public boolean hasKey(@NotNull final String key) {
		String tempKey = this.getTempKey(key);

		return this.flatFile.hasKey(tempKey);
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
	public Set<String> keySet(@NotNull final String key) {
		String tempKey = this.getTempKey(key);

		return this.flatFile.keySet(tempKey);
	}

	@Override
	public Set<String> singleLayerKeySet(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.flatFile.singleLayerKeySet(tempKey);
	}

	protected FlatSection getSectionInstance() {
		return this;
	}

	@Override
	public String toString() {
		return "SectionKey: " + this.sectionKey + ", File: " + this.flatFile.toString();
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