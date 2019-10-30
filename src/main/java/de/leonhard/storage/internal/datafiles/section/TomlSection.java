package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.Section;
import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class TomlSection extends Section {

	public TomlSection(@NotNull final TomlFile tomlFile, @NotNull final String sectionKey) {
		super(tomlFile, sectionKey);
	}

	protected TomlSection getTomlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlSection tomlSection = (TomlSection) obj;
			return super.equals(tomlSection.getSectionInstance());
		}
	}
}