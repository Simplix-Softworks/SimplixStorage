package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.Cleanup;
import lombok.Getter;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Json extends FlatFile {

	public Json(Json json) {
		super(json.getFile(), json.fileType);
		fileData = json.getFileData();
	}

	public Json(String name, String path) {
		this(name, path, null);
	}

	public Json(String name, String path, InputStream inputStream) {
		this(name, path, inputStream, null);
	}

	public Json(String name, String path, InputStream inputStream, ReloadSettings reloadSettings) {
		super(name, path, FileType.JSON);

		if (create() || file.length() == 0) {
			if (inputStream != null) {
				FileUtils.writeToFile(file, inputStream);
			}

		}

		if (reloadSettings != null) {
			this.reloadSettings = reloadSettings;
		}
		forceReload();
	}

	public Json(File file) {
		super(file, FileType.JSON);
		create();
		try {
			readToMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public Map getMap(String key) {
		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		if (!contains(finalKey)) {
			return new HashMap();
		} else {
			Object map = get(key);
			if (map instanceof Map) {
				return (Map<?, ?>) fileData.get(key);
			} else if (map instanceof JSONObject) {
				return ((JSONObject) map).toMap();
			}
			//Exception in casting
			throw new IllegalArgumentException("ClassCastEx: Json contains key: '" + key + "' but it is not a Map");
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Abstract methods to implement
	// ----------------------------------------------------------------------------------------------------

	@Override
	protected Map<String, Object> readToMap() throws IOException {
		if (file.length() == 0) {
			Files.write(file.toPath(), Collections.singletonList("{}"));
		}

		JSONTokener jsonTokener = new JSONTokener(FileUtils.createInputStream(file));
		return new JSONObject(jsonTokener).toMap();
	}

	@Override
	protected void write(FileData data) throws IOException {
		@Cleanup Writer writer = FileUtils.createWriter(file);
		writer.write(data.toJsonObject().toString(3));
		writer.flush();
	}
}