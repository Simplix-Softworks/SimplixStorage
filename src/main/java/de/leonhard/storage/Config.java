package de.leonhard.storage;

import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused"})
public class Config extends Yaml {

  private List<String> header;

  public Config(final Config config) {
    super(config);
  }

  public Config(final String name, final String path) {
    this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
  }

  public Config(
      final String name, @Nullable final String path,
      @Nullable final InputStream inputStream) {
    this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
  }

  public Config(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSettings,
      @Nullable final ConfigSettings configSettings,
      @Nullable final DataType dataType) {
    super(name, path, inputStream, reloadSettings, configSettings, dataType);
    setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
  }

  public Config(final File file) {
    super(file);
  }

  // ----------------------------------------------------------------------------------------------------
  // Method overridden from Yaml
  // ----------------------------------------------------------------------------------------------------

  @Override
  public Config addDefaultsFromInputStream() {
    return (Config) super.addDefaultsFromInputStream();
  }

  @Override
  public Config addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
    return (Config) super.addDefaultsFromInputStream(inputStream);
  }

  @Override
  protected final void writeWithComments() {
    final List<String> unEdited = yamlEditor.read();
    final List<String> header = yamlEditor.readHeader();
    final List<String> footer = yamlEditor.readFooter();
    write();
    header.addAll(yamlEditor.read());
    if (!header.containsAll(footer)) {
      header.addAll(footer);
    }
    write();
    yamlEditor.write(parser.parseComments(unEdited, header));
  }
}
