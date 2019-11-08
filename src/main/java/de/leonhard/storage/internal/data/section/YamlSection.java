package de.leonhard.storage.internal.data.section;

import de.leonhard.storage.internal.base.FlatSection;
import de.leonhard.storage.internal.data.raw.YamlFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.utils.basic.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class YamlSection extends FlatSection {

	private final YamlFile yamlFile;

	protected YamlSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
		super(sectionKey, yamlFile);
		this.yamlFile = yamlFile;
	}

	public synchronized void set(final @NotNull String key, final @Nullable Object value, final @NotNull Comment commentSetting) {
		this.yamlFile.set(this.getSectionKey(key), value, commentSetting);
	}

	public synchronized void remove(final @NotNull String key, final @NotNull Comment commentSetting) {
		this.yamlFile.remove(this.getSectionKey(key), commentSetting);
	}

	@Override
	public YamlSection getSection(final @NotNull String sectionKey) {
		return new YamlSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.yamlFile);
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