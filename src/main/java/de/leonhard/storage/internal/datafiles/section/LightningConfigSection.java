package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningConfigSection extends LightningSection {

	private final LightningConfig lightningConfig;

	protected LightningConfigSection(final @NotNull String sectionKey, final @NotNull LightningConfig lightningConfig) {
		super(sectionKey, lightningConfig);
		this.lightningConfig = lightningConfig;
	}

	public List<String> getHeader() {
		return this.lightningConfig.getHeader(this.sectionKey);
	}

	public void setHeader(final @Nullable List<String> header) {
		this.lightningConfig.setHeader(this.sectionKey, header);
	}

	public List<String> getFooter() {
		return this.lightningConfig.getFooter(this.sectionKey);
	}

	public void setFooter(final @Nullable List<String> footer) {
		this.lightningConfig.setFooter(this.sectionKey, footer);
	}

	public List<String> getHeader(final @NotNull String key) {
		String tempKey = this.getSectionKey(key);

		return this.lightningConfig.getHeader(tempKey);
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		String tempKey = this.getSectionKey(key);

		this.lightningConfig.setHeader(tempKey, header);
	}

	public List<String> getFooter(final @NotNull String key) {
		String tempKey = this.getSectionKey(key);

		return this.lightningConfig.getFooter(tempKey);
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		String tempKey = this.getSectionKey(key);

		this.lightningConfig.setFooter(tempKey, footer);
	}

	@Override
	public LightningConfigSection getSection(final @NotNull String sectionKey) {
		return new LightningConfigSection(this.sectionKey + "." + Objects.notNull(sectionKey, "Key must not be null"), this.lightningConfig);
	}

	protected LightningConfigSection getLightningConfigSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningConfigSection lightningConfigSection = (LightningConfigSection) obj;
			return this.lightningConfig.equals(lightningConfigSection.lightningConfig)
				   && this.sectionKey.equals(lightningConfigSection.sectionKey);
		}
	}
}