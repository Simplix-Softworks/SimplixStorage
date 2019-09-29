package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.base.*;
import de.leonhard.storage.editor.YamlEditor;
import de.leonhard.storage.editor.YamlParser;
import de.leonhard.storage.util.FileUtil;
import de.leonhard.storage.util.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class Yaml extends FlatFile implements StorageBase {
	@Setter(AccessLevel.PRIVATE)
	protected Map<String, Object> yamlObject;
	protected String pathPrefix;
	protected ReloadSettings reloadSettings;
	protected ConfigSettings configSettings;
	protected final YamlEditor yamlEditor;
	protected final YamlParser parser;

	public Yaml(String name, String path) {
		try {
			create(path, name, FileType.YAML);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		this.reloadSettings = ReloadSettings.INTELLIGENT;
		this.configSettings = ConfigSettings.skipComments;
		yamlEditor = new YamlEditor(file);
		parser = new YamlParser(yamlEditor);
		update();
	}

	public Yaml(String name, String path, ReloadSettings reloadSettings) {

		try {
			create(path, name, FileType.YAML);
			this.file = super.file;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.reloadSettings = reloadSettings;
		this.configSettings = ConfigSettings.skipComments;
		yamlEditor = new YamlEditor(file);
		parser = new YamlParser(yamlEditor);

		update();
	}

	public Yaml(final File file) {
		this.file = file;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		load(file);

		update();

		yamlEditor = new YamlEditor(file);
		parser = new YamlParser(yamlEditor);

		this.reloadSettings = ReloadSettings.INTELLIGENT;
		this.configSettings = ConfigSettings.skipComments;
	}

	@Override
	public void set(String key, Object value) {
		insert(key, value, this.configSettings);
	}

	public void set(String key, Object value, ConfigSettings configSettings) {
		insert(key, value, configSettings);
	}

	private void insert(String key, Object value, ConfigSettings configSettings) {
		reload();

		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		synchronized (this) {

			String old = yamlObject.toString();
			yamlObject = Utils.stringToMap(key, value, yamlObject);

			if (old.equals(yamlObject.toString()) && yamlObject != null)
				return;

			try {
				if (configSettings.equals(ConfigSettings.preserveComments)) {

					final List<String> unEdited = yamlEditor.read();
					final List<String> header = yamlEditor.readHeader();
					final List<String> footer = yamlEditor.readFooter();
					write(yamlObject);
					final List<String> lines = header;
					lines.addAll(yamlEditor.read());
					if (!header.containsAll(footer))
						lines.addAll(footer);
					yamlEditor.write(parser.parseComments(unEdited, lines));
					return;
				}
				write(yamlObject);

			} catch (final IOException e) {
				System.err.println("Error while writing '" + file.getName() + "'");
			}
			old = null;
		}
	}

	public void write(Map data) throws IOException {
		YamlWriter writer = new YamlWriter(new FileWriter(file));
		writer.write(data);
		writer.close();
	}

	@Override
	public Object get(final String key) {
		reload();
		String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
		return Utils.get(finalKey, yamlObject);
	}

	@Override
	public boolean contains(String key) {
		reload();
		key = (pathPrefix == null) ? key : pathPrefix + "." + key;
		return has(key);
	}

	private boolean has(String key) {
		reload();

		if (key.contains(".")) {
			return Utils.contains(key, yamlObject);
		}

		return yamlObject.containsKey(key);
	}

	protected void reload() {

		if (reloadSettings.equals(ReloadSettings.MANUALLY))
			return;

		if (ReloadSettings.INTELLIGENT.equals(reloadSettings))
			if (FileUtil.hasNotChanged(file, lastModified))
				return;

		update();
	}

	@Override
	public void update() {
		YamlReader reader = null;
		try {
			reader = new YamlReader(new FileReader(file));//Needed?
			if (file.length() == 0)
				yamlObject = new HashMap<>();
			else
				yamlObject = (Map<String, Object>) reader.read();
		} catch (IOException e) {
			System.err.println("Exception while reloading yaml");
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
				if (yamlObject == null) yamlObject = new HashMap<>();
			} catch (IOException e) {
				System.err.println("Exception while closing file");
				e.printStackTrace();
			}
		}
	}

	private Object getNotNested(String key) {
		if (key.contains(".")) {
			String[] parts = key.split("\\.");
			HashMap result = (HashMap) getNotNested(parts[0]);
			return result.containsKey(parts[1]) ? result.get(parts[1]) : null;
		}
		return yamlObject.getOrDefault(key, null);
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		reload();
	}

	public List<String> getHeader() {
		try {
			return yamlEditor.readHeader();
		} catch (IOException e) {
			System.err.println("Error while getting header of '" + file.getName() + "'");
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public void removeKey(final String key) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		if (finalKey.contains(".")) {
			remove(key);
			return;
		}

		final Map obj = yamlObject;
		obj.remove(key);

		try {
			write(yamlObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getKeySet() {
		reload();
		return yamlObject.keySet();
	}

	public void remove(final String key) {
		final String finalKey = (pathPrefix == null || pathPrefix.isEmpty()) ? key : pathPrefix + "." + key;
		if (!finalKey.contains(".")) {
			removeKey(key);
			return;
		}
		final Map<String, Object> old = yamlObject;
		yamlObject = Utils.remove(old, finalKey);
		try {
			write(yamlObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setConfigSettings(final ConfigSettings configSettings) {
		this.configSettings = configSettings;
	}
}

