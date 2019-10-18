package de.leonhard.storage.internal.datafiles.config;

import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.base.exceptions.InvalidSettingException;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.enums.ConfigSettings;
import de.leonhard.storage.internal.enums.ReloadSettings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"unused"})
public class YamlConfig extends YamlFile {

	private List<String> header;

	public YamlConfig(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		super(file, inputStream, reloadSettings);
		this.setConfigSettings(ConfigSettings.preserveComments);
	}


	public List<String> getHeader() {
		if (getConfigSettings().equals(ConfigSettings.skipComments)) {
			return new ArrayList<>();
		}

		try {
			if (!shouldReload()) {
				return header;
			}
		} catch (InvalidSettingException e) {
			e.printStackTrace();
		}
		try {
			return getYamlEditor().readHeader();
		} catch (IOException e) {
			System.err.println("Couldn't get header of '" + getFile().getName() + "'.");
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unused")
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
				getYamlEditor().write(this.header);
			} catch (IOException e) {
				System.err.println("Error while setting header of '" + getName() + "'");
				e.printStackTrace();
			}
			return;
		}

		try {
			final List<String> lines = getYamlEditor().read();

			final List<String> oldHeader = getYamlEditor().readHeader();
			final List<String> footer = getYamlEditor().readFooter();
			lines.removeAll(oldHeader);
			lines.removeAll(footer);

			lines.addAll(header);

			lines.addAll(footer);

			getYamlEditor().write(lines);
		} catch (final IOException e) {
			System.err.println("Exception while modifying header of '" + getName() + "'");
			e.printStackTrace();
		}
	}


	protected final YamlConfig getConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlConfig config = (YamlConfig) obj;
			return this.header.equals(config.header)
				   && super.equals(config.getYamlInstance());
		}
	}
}