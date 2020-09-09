package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.toml.TomlManager;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import lombok.NonNull;

public class Toml extends FlatFile {

  public Toml(@NonNull final Toml toml) {
    super(toml.getFile());
    this.fileData = toml.getFileData();
    this.pathPrefix = toml.getPathPrefix();
  }

  public Toml(final String name, final String path) {
    this(name, path, null);
  }

  public Toml(final String name, final String path, final InputStream inputStream) {
    this(name, path, inputStream, null);
  }

  public Toml(
      final String name,
      final String path,
      final InputStream inputStream,
      final ReloadSettings reloadSettings) {
    super(name, path, FileType.TOML);

    if (create() && inputStream != null) {
      FileUtils.writeToFile(this.file, inputStream);
    }

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }

    forceReload();
  }

  public Toml(final File file) {
    super(file, FileType.TOML);
    create();
    forceReload();
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final Map<String, Object> readToMap() throws IOException {
    return TomlManager.read(getFile());
  }

  @Override
  protected final void write(final FileData data) {
    try {
      TomlManager.write(data.toMap(), getFile());
    } catch (final IOException ioException) {
      System.err.println("Exception while writing fileData to file '" + getName() + "'");
      System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");
      ioException.printStackTrace();
    }
  }
}
