package de.leonhard.storage.internal.data.raw;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.internal.base.CommentEnabledFile;
import de.leonhard.storage.internal.base.FileType;
import de.leonhard.storage.internal.base.interfaces.CommentBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.base.interfaces.ReloadBase;
import de.leonhard.storage.internal.data.section.YamlSection;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.YamlUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import de.leonhard.storage.internal.utils.editor.YamlEditor;
import java.io.*;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Yaml-Type Files
 */
@SuppressWarnings({"unchecked", "unused"})
public class YamlFile extends CommentEnabledFile {

	protected final YamlEditor yamlEditor;
	private final YamlUtils yamlUtils;

	protected YamlFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadBase reloadSetting, final @Nullable CommentBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, FileType.YAML);

		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (reloadSetting != null) {
			this.setReloadSetting(reloadSetting);
		}
		if (commentSetting != null) {
			this.setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			this.setDataType(dataType);
		} else {
			this.setDataType(DataType.STANDARD);
		}


		this.yamlEditor = new LocalEditor(this.file);
		this.yamlUtils = new LocalUtils(yamlEditor);

		try {
			this.fileData = new LocalFileData((Map<String, Object>) new YamlReader(new FileReader(this.file)).read());
			this.lastLoaded = System.currentTimeMillis();
		} catch (YamlException | FileNotFoundException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}


	@Override
	public void reload() {
		try {
			fileData.loadData((Map<String, Object>) new YamlReader(new FileReader(this.file)).read());
			this.lastLoaded = System.currentTimeMillis();
		} catch (IOException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		update();
		return fileData.get(key);
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		update();

		if (fileData.containsKey(key)) {
			fileData.remove(key);

			try {
				write(fileData.toMap());
			} catch (IOException e) {
				System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String key : keys) {
			fileData.remove(key);
		}

		try {
			write(fileData.toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String tempKey : keys) {
			fileData.remove(key + "." + tempKey);
		}

		try {
			write(fileData.toMap());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			writeData();
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			writeData();
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			writeData();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public YamlSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	protected final YamlFile getYamlFileInstance() {
		return this;
	}

	private void write(final @NotNull Map fileData) throws IOException {
		@Cleanup YamlWriter writer = new YamlWriter(new FileWriter(this.file));
		writer.write(fileData);
	}

	private void writeData() {
		try {
			if (this.getCommentSetting() != Comment.PRESERVE) {
				write(Objects.notNull(fileData, "FileData must not be null").toMap());
			} else {
				final List<String> unEdited = yamlEditor.read();
				final List<String> header = yamlEditor.readHeader();
				final List<String> footer = yamlEditor.readFooter();
				write(fileData.toMap());
				header.addAll(yamlEditor.read());
				if (!header.containsAll(footer)) {
					header.addAll(footer);
				}
				yamlEditor.write(yamlUtils.parseComments(unEdited, header));
				write(Objects.notNull(fileData, "FileData must not be null").toMap());
			}
		} catch (IOException e) {
			System.err.println("Error while writing to '" + getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlFile yaml = (YamlFile) obj;
			return this.getCommentSetting() == yaml.getCommentSetting()
				   && super.equals(yaml.getFlatFileInstance());
		}
	}


	private static class LocalSection extends de.leonhard.storage.internal.data.section.YamlSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull YamlFile yamlFile) {
			super(sectionKey, yamlFile);
		}
	}


	private static class LocalEditor extends YamlEditor {

		private LocalEditor(final File file) {
			super(file);
		}
	}

	private static class LocalUtils extends YamlUtils {

		private LocalUtils(final YamlEditor yamlEditor) {
			super(yamlEditor);
		}
	}
}