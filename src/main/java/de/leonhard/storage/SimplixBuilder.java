package de.leonhard.storage;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.provider.InputStreamProvider;
import de.leonhard.storage.internal.provider.SimplixProviders;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public final class SimplixBuilder {

  private final InputStreamProvider inputStreamProvider;

  private final String path;
  private String name;
  private InputStream inputStream;
  private ReloadSettings reloadSettings;
  private ConfigSettings configSettings;
  private DataType dataType;

  private @Nullable Consumer<FlatFile> reloadConsumer = null;

  private SimplixBuilder(
      final String name, final String path, final InputStreamProvider inputStreamProvider) {
    this.name = name;
    this.path = path;
    this.inputStreamProvider = inputStreamProvider;
  }

  // ----------------------------------------------------------------------------------------------------
  // Creating our Builder
  // ----------------------------------------------------------------------------------------------------

  public static SimplixBuilder fromPath(@NonNull final String name, @NonNull final String path) {
    return new SimplixBuilder(name, path, SimplixProviders.inputStreamProvider());
  }

  public static SimplixBuilder fromPath(@NonNull final Path path) {
    return fromFile(path.toFile());
  }

  public static SimplixBuilder fromFile(@NonNull final File file) {
    // File shouldn't be a directory
    Valid.checkBoolean(
        !file.isDirectory(),
        "File mustn't be a directory.",
        "Please use from Directory to use a directory",
        "This is due to Java-Internals");

    return new SimplixBuilder(
        FileUtils.replaceExtensions(file.getName()),
        FileUtils.getParentDirPath(file),
        SimplixProviders.inputStreamProvider());
  }

  public static SimplixBuilder fromDirectory(@NonNull final File file) {
    Valid.checkBoolean(!file.getName().contains("."), "File-Name mustn't contain '.'");

    if (!file.exists()) {
      file.mkdirs();
    }

    // Will return the name of the folder as default name
    return new SimplixBuilder(
        file.getName(), file.getAbsolutePath(), SimplixProviders.inputStreamProvider());
  }

  // ----------------------------------------------------------------------------------------------------
  // Adding out settings
  // ----------------------------------------------------------------------------------------------------

  public SimplixBuilder reloadCallback(@Nullable final Consumer<FlatFile> reloadConsumer) {
    this.reloadConsumer = reloadConsumer;
    return this;
  }

  public SimplixBuilder addInputStreamFromFile(@NonNull final File file) {
    this.inputStream = FileUtils.createInputStream(file);
    return this;
  }

  public SimplixBuilder addInputStreamFromResource(@NonNull final String resource) {
    this.inputStream = this.inputStreamProvider.createInputStreamFromInnerResource(resource);

    Valid.notNull(
        this.inputStream, "InputStream is null.", "No inbuilt resource '" + resource + "' found: ");
    return this;
  }

  public SimplixBuilder setName(@NonNull final String name) {
    this.name = name;
    return this;
  }

  public SimplixBuilder addInputStream(@Nullable final InputStream inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  public SimplixBuilder setConfigSettings(@NonNull final ConfigSettings configSettings) {
    this.configSettings = configSettings;
    return this;
  }

  public SimplixBuilder setReloadSettings(@NonNull final ReloadSettings reloadSettings) {
    this.reloadSettings = reloadSettings;
    return this;
  }

  public SimplixBuilder setDataType(@NonNull final DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  // ----------------------------------------------------------------------------------------------------
  // Create the objects of our FileTypes
  // ----------------------------------------------------------------------------------------------------

  public Config createConfig() {
    return new Config(
        this.name,
        this.path,
        this.inputStream,
        this.reloadSettings,
        this.configSettings,
        this.dataType,
        reloadConsumer);
  }

  public Yaml createYaml() {
    return new Yaml(
        this.name,
        this.path,
        this.inputStream,
        this.reloadSettings,
        this.configSettings,
        this.dataType,
        reloadConsumer);
  }

  public Toml createToml() {
    return new Toml(
        this.name,
        this.path,
        this.inputStream,
        this.reloadSettings,
        reloadConsumer);
  }

  public Json createJson() {
    return new Json(
        this.name,
        this.path,
        this.inputStream,
        this.reloadSettings,
        reloadConsumer);
  }
}
