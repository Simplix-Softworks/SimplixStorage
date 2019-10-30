package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningConfigSection extends LightningSection {

	private final LightningConfig lightningConfig;

	public LightningConfigSection(final @NotNull LightningConfig lightningConfig, final @NotNull String sectionKey) {
		super(lightningConfig, sectionKey);
		this.lightningConfig = lightningConfig;
	}

	public List<String> getHeader() {
		return this.lightningConfig.getHeader(this.sectionKey);
	}

	public void setHeader(@Nullable final List<String> header) {
		this.lightningConfig.setHeader(this.sectionKey, header);
	}

	public List<String> getFooter() {
		return this.lightningConfig.getFooter(this.sectionKey);
	}

	public void setFooter(@Nullable final List<String> footer) {
		this.lightningConfig.setFooter(this.sectionKey, footer);
	}

	public List<String> getHeader(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningConfig.getHeader(tempKey);
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningConfig.setHeader(tempKey, header);
	}

	public List<String> getFooter(@NotNull final String key) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		return this.lightningConfig.getFooter(tempKey);
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		String tempKey = (this.sectionKey == null || this.sectionKey.isEmpty()) ? key : this.sectionKey + "." + key;

		this.lightningConfig.setFooter(tempKey, footer);
	}

	protected LightningConfigSection getLightningConfigSectionInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningConfigSection lightningConfigSection = (LightningConfigSection) obj;
			return this.lightningConfig.equals(lightningConfigSection.lightningConfig)
				   && super.equals(lightningConfigSection.getLightningSectionInstance());
		}
	}
}