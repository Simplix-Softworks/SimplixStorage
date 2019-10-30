package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.JsonFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class JsonSection extends FlatSection {

	public JsonSection(final @NotNull JsonFile jsonFile, final @NotNull String sectionKey) {
		super(jsonFile, sectionKey);
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonSection jsonSection = (JsonSection) obj;
			return super.equals(jsonSection.getSectionInstance());
		}
	}
}