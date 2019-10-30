package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.enums.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningSection extends FlatSection {

	private final LightningFile lightningFile;

	public LightningSection(@NotNull final LightningFile lightningFile, @NotNull final String sectionKey) {
		super(lightningFile, sectionKey);
		this.lightningFile = lightningFile;
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.set(tempKey, value, commentSetting);
	}

	public synchronized void remove(@NotNull final String key, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningFile.remove(tempKey, commentSetting);
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
				   && super.equals(lightningSection.getSectionInstance());
		}
	}
}