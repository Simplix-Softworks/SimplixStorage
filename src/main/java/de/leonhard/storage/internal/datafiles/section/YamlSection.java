package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class YamlSection extends FlatSection {

	private final YamlFile yamlFile;

	public YamlSection(@NotNull final YamlFile yamlFile, @NotNull final String sectionKey) {
		super(yamlFile, sectionKey);
		this.yamlFile = yamlFile;
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.yamlFile.set(tempKey, value, preserveComments);
	}

	public synchronized void remove(@NotNull final String key, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.yamlFile.remove(tempKey, preserveComments);
	}

	@Override
	public YamlSection getSection(@NotNull final String sectionKey) {
		return new YamlSection(this.yamlFile, this.sectionKey + "." + sectionKey);
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