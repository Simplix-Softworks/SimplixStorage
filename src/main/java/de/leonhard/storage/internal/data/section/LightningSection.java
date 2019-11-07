package de.leonhard.storage.internal.data.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.data.raw.LightningFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningSection extends FlatSection {

	private final LightningFile lightningFile;

	protected LightningSection(final @NotNull String sectionKey, final @NotNull LightningFile lightningFile) {
		super(sectionKey, lightningFile);
		this.lightningFile = lightningFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull Comment commentSetting) {
		String tempKey = this.getSectionKey(key);

		this.lightningFile.set(tempKey, value, commentSetting);
	}

	public synchronized void remove(final @NotNull String key, final @NotNull Comment commentSetting) {
		String tempKey = this.getSectionKey(key);

		this.lightningFile.remove(tempKey, commentSetting);
	}

	@Override
	public LightningSection getSection(final @NotNull String sectionKey) {
		return new LightningSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.lightningFile);
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