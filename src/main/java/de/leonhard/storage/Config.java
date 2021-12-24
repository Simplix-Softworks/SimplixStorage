package de.leonhard.storage;

import de.leonhard.storage.annotation.ConfigPath;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unused"})
public class Config extends Yaml {

  public Config(@NonNull final Config config) {
    super(config);
  }

  public Config(final String name, final String path) {
    this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
  }

  public Config(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream) {
    this(name, path, inputStream, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
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

  public Config(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSettings,
      @Nullable final ConfigSettings configSettings,
      @Nullable final DataType dataType,
      @Nullable final Consumer<FlatFile> reloadConsumer) {
    super(name, path, inputStream, reloadSettings, configSettings, dataType, reloadConsumer);
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
}
