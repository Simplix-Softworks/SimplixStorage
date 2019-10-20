package de.leonhard.storage.internal.datafiles;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.editor.YamlEditor;
import de.leonhard.storage.internal.editor.YamlParser;
import de.leonhard.storage.internal.enums.ConfigSettings;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;


@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
@Getter
public class YamlFile extends FlatFile {

	private final YamlParser parser;
	private final YamlEditor yamlEditor;
	private YamlReader reader;
	@Setter
	private ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;


	protected YamlFile(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		if (FileTypeUtils.isType(file, FileType.YAML)) {
			if (create(file)) {
				if (inputStream != null) {
					FileUtils.writeToFile(this.file, inputStream);
				}
			}

			yamlEditor = new YamlEditor(this.file);
			parser = new YamlParser(yamlEditor);
			update();
			if (reloadSettings != null) {
				setReloadSettings(reloadSettings);
			}
		} else {
			throw new InvalidFileTypeException("The given file if of no valid filetype.");
		}
	}

	@Override
	public void reload() {
		try {
			reader = new YamlReader(new FileReader(this.file));
			Map<String, Object> map = (Map<String, Object>) reader.read();
			if (map == null) {
				map = new HashMap<>();
			}
			fileData = new FileData(map);
		} catch (IOException e) {
			System.err.println("Exception while reading yaml");
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.err.println("Exception while closing file");
				e.printStackTrace();
			}
		}
	}

	@Override
	public <T> T getOrSetDefault(final String path, T def) {
		update();
		if (!hasKey(path)) {
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

	@SuppressWarnings("Duplicates")
	public synchronized void set(final String key, final Object value, final ConfigSettings configSettings) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		String oldData = fileData.toString();
		fileData.insert(finalKey, value);

		if (!oldData.equals(fileData.toString())) {
			try {
				if (!ConfigSettings.PRESERVE_COMMENTS.equals(configSettings)) {
					write(Objects.requireNonNull(fileData).toMap());
					return;
				}
				final List<String> unEdited = yamlEditor.read();
				final List<String> header = yamlEditor.readHeader();
				final List<String> footer = yamlEditor.readFooter();
				write(fileData.toMap());
				header.addAll(yamlEditor.read());
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				yamlEditor.write(parser.parseComments(unEdited, header));
				write(Objects.requireNonNull(fileData).toMap());
			} catch (final IOException e) {
				System.err.println("Error while writing '" + getName() + "'");
			}
		}
	}

	private void write(final Map data) throws IOException {
		YamlWriter writer = new YamlWriter(new FileWriter(this.file));
		writer.write(data);
		writer.close();
	}

	@Override
	public Object get(final String key) {
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@Override
	public synchronized void remove(final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		fileData.remove(finalKey);

		try {
			write(fileData.toMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(final String key, final Object value) {
		set(key, value, this.configSettings);
	}

	@Override
	public void setDefault(final String key, final Object value) {
		if (!hasKey(key)) {
			set(key, value, getConfigSettings());
		}
	}

	protected final YamlFile getYamlInstance() {
		return this;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlFile yaml = (YamlFile) obj;
			return this.configSettings.equals(yaml.configSettings)
				   && super.equals(yaml.getFlatFileInstance());
		}
	}
}