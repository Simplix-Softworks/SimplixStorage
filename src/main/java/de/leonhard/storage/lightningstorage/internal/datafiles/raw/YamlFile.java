package de.leonhard.storage.lightningstorage.internal.datafiles.raw;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.lightningstorage.editor.YamlEditor;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.internal.base.enums.ConfigSetting;
import de.leonhard.storage.lightningstorage.internal.base.enums.ReloadSetting;
import de.leonhard.storage.lightningstorage.utils.FileUtils;
import de.leonhard.storage.lightningstorage.utils.YamlUtils;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unchecked", "unused"})
public class YamlFile extends FlatFile {

	protected final YamlEditor yamlEditor;
	private final YamlUtils parser;

	public YamlFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final ReloadSetting reloadSetting, @Nullable final ConfigSetting configSetting, @Nullable final FileData.Type fileDataType) {
		super(file, FileType.YAML);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (configSetting != null) {
			setConfigSetting(configSetting);
		}
		if (fileDataType != null) {
			setFileDataType(fileDataType);
		} else {
			setFileDataType(FileData.Type.STANDARD);
		}

		this.yamlEditor = new YamlEditor(this.file);
		this.parser = new YamlUtils(yamlEditor);
		reload();
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}
	}


	@Override
	public void reload() {
		try {
			@Cleanup YamlReader reader = new YamlReader(new FileReader(this.file));
			Map<String, Object> map = (Map<String, Object>) reader.read();
			fileData = new FileData(map);
		} catch (IOException e) {
			System.err.println("Exception while reading YAML");
			e.printStackTrace();
		}
	}

	@Override
	public Object get(@NotNull final String key) {
		update();
		String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;
		return fileData.get(finalKey);
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		final String finalKey = (this.getPathPrefix() == null) ? key : this.getPathPrefix() + "." + key;

		update();

		if (fileData.containsKey(finalKey)) {
			fileData.remove(finalKey);

			try {
				write(fileData.toMap());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(@NotNull final Map fileData) throws IOException {
		@Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.file));
		writer.write(fileData);
	}

	@Override
	public void set(@NotNull final String key, @Nullable final Object value) {
		set(key, value, this.getConfigSetting());
	}

	@SuppressWarnings("DuplicatedCode")
	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final ConfigSetting configSetting) {
		if (insert(key, value)) {
			try {
				if (!ConfigSetting.PRESERVE_COMMENTS.equals(configSetting)) {
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
			} catch (IOException e) {
				System.err.println("Error while writing to '" + file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	protected final YamlFile getYamlFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlFile yaml = (YamlFile) obj;
			return this.getConfigSetting().equals(yaml.getConfigSetting())
				   && super.equals(yaml.getFlatFileInstance());
		}
	}
}