package de.leonhard.storage;

import de.leonhard.storage.internal.enums.ConfigSettings;
import de.leonhard.storage.internal.enums.ReloadSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Config extends Yaml {
	private List<String> header;

	public Config(String name, String path, ReloadSettings reloadSettings) {
		super(name, path, reloadSettings);
	}

	public Config(String name, String path) {
		super(name, path);
	}

	public Config(String name, String path, FileInputStream fileInputStream) {
		super(name, path, fileInputStream);
	}

	public Config(String name, String path, InputStream inputStream, ReloadSettings reloadSettings) {
		super(name, path, inputStream, reloadSettings);
	}


	@Override
	public void set(final String key, final Object value) {
		super.set(key, value, getConfigSettings());
	}

	@Override
	public void setDefault(final String key, final Object value) {
		if (!contains(key)) {
			set(key, value, getConfigSettings());
		}
	}

	@Override
	public <T> T getOrSetDefault(final String path, T def) {
		reload();
		if (!contains(path)) {
			set(path, def, getConfigSettings());
			return def;
		} else {
			Object obj = get(path); //
			if (obj instanceof String && def instanceof Integer) {
				obj = Integer.parseInt((String) obj);
			}
			if (obj instanceof String && def instanceof Double) {
				obj = Double.parseDouble((String) obj);
			}
			if (obj instanceof String && def instanceof Float) {
				obj = Double.parseDouble((String) obj);
			}
			return (T) obj;
		}
	}


	@Override
	public List<String> getHeader() {

		if (getConfigSettings().equals(ConfigSettings.skipComments)) {
			return new ArrayList<>();
		}

		if (!shouldReload()) {
			return header;
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

	protected Config getConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			Config config = (Config) obj;
			return this.header.equals(config.header)
					&& super.equals(config.getYamlInstance());
		}
	}
}
