package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.enums.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class YamlSection extends FlatSection {

	private final YamlFile yamlFile;

	public YamlSection(@NotNull final YamlFile yamlFile, @NotNull final String sectionKey) {
		super(yamlFile, sectionKey);
		this.yamlFile = yamlFile;
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.yamlFile.set(tempKey, value, commentSetting);
	}

	protected YamlSection getYamlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlSection yamlSection = (YamlSection) obj;
			return this.yamlFile.equals(yamlSection.yamlFile)
				   && this.sectionKey.equals(yamlSection.sectionKey);
		}
	}
}