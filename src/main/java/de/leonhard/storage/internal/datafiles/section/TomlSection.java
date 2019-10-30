package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class TomlSection extends FlatSection {

	private final TomlFile tomlFile;

	public TomlSection(@NotNull final TomlFile tomlFile, @NotNull final String sectionKey) {
		super(tomlFile, sectionKey);
		this.tomlFile = tomlFile;
	}

	@Override
	public TomlSection getSection(@NotNull final String sectionKey) {
		return new TomlSection(this.tomlFile, this.sectionKey + "." + sectionKey);
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