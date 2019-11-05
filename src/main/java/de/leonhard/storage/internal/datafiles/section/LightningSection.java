package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningSection extends FlatSection {

	private final LightningFile lightningFile;

	protected LightningSection(@NotNull final LightningFile lightningFile, @NotNull final String sectionKey) {
		super(lightningFile, sectionKey);
		this.lightningFile = lightningFile;
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.lightningFile.set(tempKey, value, preserveComments);
	}

	public synchronized void remove(@NotNull final String key, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.lightningFile.remove(tempKey, preserveComments);
	}

	@Override
	public LightningSection getSection(final @NotNull String sectionKey) {
		return new LightningSection(this.lightningFile, this.sectionKey + "." + sectionKey);
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
			return this.lightningFile.equals(lightningSection.lightningFile)
				   && this.sectionKey.equals(lightningSection.sectionKey);
		}
	}
}