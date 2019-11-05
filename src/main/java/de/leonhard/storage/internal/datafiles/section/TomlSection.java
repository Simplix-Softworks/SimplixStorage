package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.TomlFile;
import de.leonhard.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class TomlSection extends FlatSection {

	private final TomlFile tomlFile;

	protected TomlSection(final @NotNull TomlFile tomlFile, final @NotNull String sectionKey) {
		super(tomlFile, sectionKey);
		this.tomlFile = tomlFile;
	}

	@Override
	public TomlSection getSection(final @NotNull String sectionKey) {
		return new TomlSection(this.tomlFile, this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"));
	}

	protected TomlSection getTomlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			TomlSection tomlSection = (TomlSection) obj;
			return this.tomlFile.equals(tomlSection.tomlFile)
				   && this.sectionKey.equals(tomlSection.sectionKey);
		}
	}
}