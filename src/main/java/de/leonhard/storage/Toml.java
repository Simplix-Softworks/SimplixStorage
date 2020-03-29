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

public class Toml extends FlatFile {

  public Toml(final Toml toml) {
    super(toml.getFile());
    fileData = toml.getFileData();
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
      FileUtils.writeToFile(file, inputStream);
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
    } catch (final IOException ex) {
      System.err.println("Exception while writing fileData to file '" + getName() + "'");
      System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
      ex.printStackTrace();
    }
  }
}
