package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.LightningFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningSection extends FlatSection {

	private final LightningFile lightningFile;

	protected LightningSection(final @NotNull LightningFile lightningFile, final @NotNull String sectionKey) {
		super(lightningFile, sectionKey);
		this.lightningFile = lightningFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull Comment commentSetting) {
		String tempKey = this.getTempKey(key);

		this.lightningFile.set(tempKey, value, commentSetting);
	}

	public synchronized void remove(final @NotNull String key, final @NotNull Comment commentSetting) {
		String tempKey = this.getTempKey(key);

		this.lightningFile.remove(tempKey, commentSetting);
	}

	@Override
	public LightningSection getSection(final @NotNull String sectionKey) {
		return new LightningSection(this.lightningFile, this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"));
	}

	protected LightningSection getLightningSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
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