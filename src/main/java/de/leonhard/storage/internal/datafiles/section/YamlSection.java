package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.utils.basic.Valid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class YamlSection extends FlatSection {

	private final YamlFile yamlFile;

	protected YamlSection(final @NotNull YamlFile yamlFile, final @NotNull String sectionKey) {
		super(yamlFile, sectionKey);
		this.yamlFile = yamlFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.yamlFile.set(tempKey, value, preserveComments);
	}

	public synchronized void remove(final @NotNull String key, final boolean preserveComments) {
		String tempKey = this.getTempKey(key);

		this.yamlFile.remove(tempKey, preserveComments);
	}

	@Override
	public YamlSection getSection(final @NotNull String sectionKey) {
		return new YamlSection(this.yamlFile, this.sectionKey + "." + Valid.notNullObject(sectionKey, "Key must not be null"));
	}

	protected YamlSection getYamlSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
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