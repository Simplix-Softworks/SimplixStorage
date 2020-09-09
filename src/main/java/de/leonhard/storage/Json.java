package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Cleanup;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

@Getter
public class Json extends FlatFile {

  public Json(final Json json) {
    super(json.getFile(), json.fileType);
    this.fileData = json.getFileData();
    this.pathPrefix = json.getPathPrefix();
  }

  public Json(final String name, final String path) {
    this(name, path, null);
  }

  public Json(final String name, final String path, final InputStream inputStream) {
    this(name, path, inputStream, null);
  }

  public Json(
      final String name,
      @Nullable final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSettings) {
    super(name, path, FileType.JSON);

    if (create() || this.file.length() == 0) {
      if (inputStream != null) {
        FileUtils.writeToFile(this.file, inputStream);
      }
    }

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }
    forceReload();
  }

  public Json(final File file) {
    super(file, FileType.JSON);
    create();
    forceReload();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override (Points where JSON is unspecific for typical FlatFiles)
  // ----------------------------------------------------------------------------------------------------

  /**
   * Gets a Map by key Although used to get nested objects {@link Json}
   *
   * @param key Path to Map-List in JSON
   * @return Map
   */
  @Override
  public Map getMap(final String key) {
    final String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
    if (!contains(finalKey)) {
      return new HashMap<>();
    } else {
      final Object map = get(key);
      if (map instanceof Map) {
        return (Map<?, ?>) this.fileData.get(key);
      } else if (map instanceof JSONObject) {
        return ((JSONObject) map).toMap();
      }
      // Exception in casting
      throw new IllegalArgumentException(
          "ClassCastEx: Json contains key: '" + key + "' but it is not a Map");
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected Map<String, Object> readToMap() throws IOException {
    if (this.file.length() == 0) {
      Files.write(this.file.toPath(), Collections.singletonList("{}"));
    }

    final JSONTokener jsonTokener = new JSONTokener(FileUtils.createInputStream(this.file));
    return new JSONObject(jsonTokener).toMap();
  }

  @Override
  protected void write(final FileData data) throws IOException {
    @Cleanup final Writer writer = FileUtils.createWriter(this.file);
    writer.write(data.toJsonObject().toString(3));
    writer.flush();
  }
}
