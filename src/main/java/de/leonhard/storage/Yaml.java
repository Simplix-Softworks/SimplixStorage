package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlException;
import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.yaml.YamlEditor;
import de.leonhard.storage.internal.editor.yaml.YamlParser;
import de.leonhard.storage.internal.editor.yaml.YamlReader;
import de.leonhard.storage.internal.editor.yaml.YamlWriter;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.jetbrains.annotations.Nullable;

@Getter
public class Yaml extends FlatFile {

  protected final InputStream inputStream;
  protected final YamlEditor yamlEditor;
  protected final YamlParser parser;
  @Setter
  private ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;

  public Yaml(final Yaml yaml) {
    super(yaml.getFile());
    fileData = yaml.getFileData();
    yamlEditor = yaml.getYamlEditor();
    parser = yaml.getParser();
    configSettings = yaml.getConfigSettings();
    inputStream = yaml.getInputStream().orElse(null);
  }

  public Yaml(final String name, @Nullable final String path) {
    this(name, path, null, null, null, null);
  }

  public Yaml(
      final String name, @Nullable final String path,
      @Nullable final InputStream inputStream) {
    this(name, path, inputStream, null, null, null);
  }

  public Yaml(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSettings,
      @Nullable final ConfigSettings configSettings,
      @Nullable final DataType dataType) {
    super(name, path, FileType.YAML);
    this.inputStream = inputStream;

    if (create() && inputStream != null) {
      FileUtils.writeToFile(file, inputStream);
    }

    yamlEditor = new YamlEditor(file);
    parser = new YamlParser(yamlEditor);

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }

    if (configSettings != null) {
      this.configSettings = configSettings;
    }

    if (dataType != null) {
      this.dataType = dataType;
    } else {
      this.dataType = DataType.fromConfigSettings(configSettings);
    }

    forceReload();
  }

  public Yaml(final File file) {
    this(file.getName(), FileUtils.getParentDirPath(file));
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override (Points where YAML is unspecific for typical FlatFiles)
  // ----------------------------------------------------------------------------------------------------

  public Yaml addDefaultsFromInputStream() {
    return addDefaultsFromInputStream(getInputStream().orElse(null));
  }

  public Yaml addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
    reloadIfNeeded();
    // Creating & setting defaults
    if (inputStream == null) {
      return this;
    }
    try {
      final Map<String, Object> data = new YamlReader(
          new InputStreamReader(inputStream)).readToMap();

      // Merging
      for (final Entry<String, Object> entry : data.entrySet()) {
        if (!fileData.containsKey(entry.getKey())) {
          fileData.insert(entry.getKey(), entry.getValue());
        }
      }
      writeWithComments();
    } catch (final YamlException e) {
      e.printStackTrace();
    }

    return this;
  }

  @Override
  @Synchronized
  public void set(final String key, final Object value) {
    reloadIfNeeded();

    final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

    fileData.insert(finalKey, value);
    writeWithComments();
  }


  /**
   * Method to write to file while preserving comments if set
   */
  protected void writeWithComments() {
    // If Comments shouldn't be preserved
    if (!ConfigSettings.PRESERVE_COMMENTS.equals(configSettings)) {
      write();
      return;
    }

    final List<String> unEdited = yamlEditor.read();
    final List<String> header = yamlEditor.readHeader();
    final List<String> footer = yamlEditor.readFooter();
    write();
    header.addAll(yamlEditor.read());
    if (!header.containsAll(footer)) {
      header.addAll(footer);
    }
    write();
    yamlEditor.write(parser.parseLines(unEdited, header));
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected Map<String, Object> readToMap() throws IOException {
    @Cleanup final YamlReader reader = new YamlReader(new FileReader(getFile()));
    return reader.readToMap();
  }

  @Override
  protected void write(final FileData data) throws IOException {
    @Cleanup final YamlWriter writer = new YamlWriter(file);
    writer.write(data.toMap());
  }

  // ----------------------------------------------------------------------------------------------------
  // Specific utility methods for YAML
  // ----------------------------------------------------------------------------------------------------

  public final List<String> getHeader() {
    return yamlEditor.readHeader();
  }

  public final void setHeader(final List<String> header) {
    yamlEditor.setHeader(header);
  }

  public final void setHeader(final String... header) {
    setHeader(Arrays.asList(header));
  }

  public final void addHeader(final List<String> toAdd) {
    yamlEditor.addHeader(toAdd);
  }

  public final void addHeader(final String... header) {
    addHeader(Arrays.asList(header));
  }

  public final Optional<InputStream> getInputStream() {
    return Optional.ofNullable(inputStream);
  }
}
