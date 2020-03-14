package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.lighningfile.LightningEditor;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Map;

/** Class to manager Lightning-Type Files */
@SuppressWarnings("unused")
@Getter
public class LightningFile extends FlatFile {

  private final LightningEditor lightningEditor;
  private ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;

  public LightningFile(final LightningFile lightningFile) {
    super(lightningFile.getFile());
    lightningEditor = lightningFile.getLightningEditor();
    configSettings = lightningFile.getConfigSettings();
  }

  public LightningFile(final String name, final String path) {
    this(name, path, null, null, null);
  }

  public LightningFile(
      final String name, @Nullable final String path, @Nullable final InputStream inputStream) {
    this(name, path, inputStream, null, null);
  }

  public LightningFile(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSetting,
      @Nullable final ConfigSettings configSettings) {
    super(name, path, FileType.LS);

    lightningEditor = new LightningEditor(file);

    if (create() && inputStream != null) {
      FileUtils.writeToFile(file, inputStream);
    }

    if (configSettings != null) {
      this.configSettings = configSettings;
    }

    if (reloadSetting != null) {
      reloadSettings = reloadSetting;
    }

    forceReload();
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected Map<String, Object> readToMap() {
    return lightningEditor.readData();
  }

  @Override
  protected void write(final FileData data) {
    lightningEditor.writeData(data.toMap(), configSettings);
  }
}
