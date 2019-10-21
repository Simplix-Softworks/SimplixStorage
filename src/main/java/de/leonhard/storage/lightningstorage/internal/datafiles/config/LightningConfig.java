package de.leonhard.storage.lightningstorage.internal.datafiles.config;

import de.leonhard.storage.lightningstorage.editor.LightningFileUtils;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.datafiles.raw.LightningFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class LightningConfig extends LightningFile {

	private final LightningFileUtils lightningFileUtils;
	private List<String> header;

	public LightningConfig(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		super(file, inputStream, reloadSetting, configSetting == null ? ConfigSetting.PRESERVE_COMMENTS : configSetting, fileDataType);
		this.lightningFileUtils = new LightningFileUtils(this.file);
	}

	public List<String> getHeader() {
		if (getConfigSetting().equals(ConfigSetting.SKIP_COMMENTS)) {
			return new ArrayList<>();
		}

		if (!shouldReload()) {
			return header;
		}
		try {
			return this.lightningFileUtils.readHeader();
		} catch (IOException e) {
			System.err.println("Couldn't get header of '" + getFile().getName() + "'.");
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@SuppressWarnings({"unused", "DuplicatedCode"})
	public void setHeader(List<String> header) {
		List<String> tmp = new ArrayList<>();
		//Updating the values to have a comments, if someone forgets to set them
		for (final String line : header) {
			if (!line.startsWith("#")) {
				tmp.add("#" + line);
			} else {
				tmp.add(line);
			}
		}
		this.header = tmp;

		if (getFile().length() == 0) {
			try {
				this.lightningFileUtils.write(this.header);
			} catch (IOException e) {
				System.err.println("Error while setting header of '" + getName() + "'");
				e.printStackTrace();
			}
			return;
		}

		try {
			final List<String> lines = this.lightningFileUtils.read();

			final List<String> oldHeader = this.lightningFileUtils.readHeader();
			final List<String> footer = this.lightningFileUtils.readFooter();
			lines.removeAll(oldHeader);
			lines.removeAll(footer);

			lines.addAll(header);

			lines.addAll(footer);

			this.lightningFileUtils.write(lines);
		} catch (final IOException e) {
			System.err.println("Exception while modifying header of '" + getName() + "'");
			e.printStackTrace();
		}
	}

	protected final LightningConfig getLightningConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningConfig config = (LightningConfig) obj;
			return this.header.equals(config.header)
				   && super.equals(config.getLightningFileInstance());
		}
	}
}