package de.leonhard.storage.internal.datafiles.raw;


import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.JsonUtils;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


@SuppressWarnings({"unchecked", "unused"})
public class JsonFile extends FlatFile {

	public JsonFile(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
		if (FileTypeUtils.isType(file, FileType.JSON)) {
			if (create(file)) {
				if (inputStream != null) {
					FileUtils.writeToFile(this.file, inputStream);
				}
			}

			update();
			if (reloadSettings != null) {
				setReloadSettings(reloadSettings);
			}
		} else {
			throw new InvalidFileTypeException("The given file if of no valid filetype.");
		}
	}

	@Override
	protected void update() {
		final JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(FileUtils.createNewInputStream(file)));
		fileData = new FileData(new JSONObject(jsonTokener));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonFile json = (JsonFile) obj;
			return super.equals(json.getFlatFileInstance());
		}
	}

	@Override
	public synchronized void remove(final String key) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		final Map obj = fileData.toMap();
		obj.remove(finalKey);

		fileData = new FileData(new JSONObject(obj));
		try {
			write(fileData.toJsonObject());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object get(final String key) {

		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		return getObject(finalKey);
	}

	/**
	 * Gets a Map by key Although used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 * @return Map
	 */

	@Override
	public Map getMap(final String key) {
		String tempKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		if (!hasKey(tempKey)) {
			return new HashMap();
		} else {
			return getMapWithoutPath(tempKey);
		}
	}

	/**
	 * Sets a value to the json if the file doesn't already contain the value
	 * (Not mix up with Bukkit addDefault) Uses {@link JSONObject}
	 *
	 * @param key   Key to set the value
	 * @param value Value to set
	 */

	@Override
	public void setDefault(String key, Object value) {
		if (hasKey(key)) {
			return;
		}
		set(key, value);
	}

	@Override
	public synchronized void set(final String key, final Object value) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		synchronized (this) {

			reload();

			String old = fileData.toString();
			fileData.insert(finalKey, value);


			if (!old.equals(fileData.toString())) {
				try {
					write(new JSONObject(fileData.toMap()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public <T> T getOrSetDefault(final String path, T def) {
		if (!hasKey(path)) {
			set(path, def);
			return def;
		} else {
			return (T) get(path);
		}
	}

	private void write(final JSONObject object) throws IOException {
		Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
		writer.write(object.toString(3));
		writer.close();
	}

	private Map getMapWithoutPath(final String key) {
		reload();

		if (!hasKey(key)) {
			return new HashMap<>();
		}

		Object map;
		try {
			map = getObject(key);
		} catch (JSONException e) {
			return new HashMap<>();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) fileData.get(key);
		} else if (map instanceof JSONObject) {
			return JsonUtils.jsonToMap((JSONObject) map);
		}
		throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
	}

	private Object getObject(final String key) {
		if (!hasKey(key)) {
			return null;
		}

		if (key.contains(".")) {
			return new FileData(fileData.toMap()).containsKey(key) ? new FileData(fileData.toMap()).get(key) : null;
		}
		return fileData.containsKey(key) ? fileData.get(key) : null;
	}

	protected final JsonFile getJsonInstance() {
		return this;
	}
}