package de.leonhard.storage.internal.datafiles.section;

import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningConfigSection extends LightningSection {

	private final LightningConfig lightningConfig;

	public LightningConfigSection(final @NotNull LightningConfig lightningConfig, final @NotNull String key) {
		super(lightningConfig, key);
		this.lightningConfig = lightningConfig;
	}

	public List<String> getHeader() {
		return this.lightningConfig.getHeader(getSectionKey());
	}

	public void setHeader(@Nullable final List<String> header) {
		this.lightningConfig.setHeader(getSectionKey(), header);
	}

	public List<String> getFooter() {
		return this.lightningConfig.getFooter(getSectionKey());
	}

	public void setFooter(@Nullable final List<String> footer) {
		this.lightningConfig.setFooter(getSectionKey(), footer);
	}

	public List<String> getHeader(@NotNull final String key) {
		String tempKey = (getSectionKey() == null || getSectionKey().isEmpty()) ? key : getSectionKey() + "." + key;

		return this.lightningConfig.getHeader(tempKey);
	}

	public void setHeader(@NotNull final String key, @Nullable final List<String> header) {
		String tempKey = (getSectionKey() == null || getSectionKey().isEmpty()) ? key : getSectionKey() + "." + key;

		this.lightningConfig.setHeader(tempKey, header);
	}

	public List<String> getFooter(@NotNull final String key) {
		String tempKey = (getSectionKey() == null || getSectionKey().isEmpty()) ? key : getSectionKey() + "." + key;

		return this.lightningConfig.getFooter(tempKey);
	}

	public void setFooter(@NotNull final String key, @Nullable final List<String> footer) {
		String tempKey = (getSectionKey() == null || getSectionKey().isEmpty()) ? key : getSectionKey() + "." + key;

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
			return super.equals(lightningConfigSection.getLightningSectionInstance());
		}
	}
}