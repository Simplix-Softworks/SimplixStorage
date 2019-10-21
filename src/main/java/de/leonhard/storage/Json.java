package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.IStorage;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import de.leonhard.storage.utils.JsonUtils;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Json extends FlatFile implements IStorage {

	public Json(final String name, final String path) {
		this(name, path, null);
	}

	public Json(final String name, final String path, final ReloadSettings reloadSettings) {
		super(name, path, FileType.JSON);
		if (create() || file.length() == 0) {
			try {
				Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
				writer.write(new JSONObject().toString(2));
				writer.close();
			} catch (final Exception ex) {
				System.err.println("Error creating JSON '" + file.getName() + "'");
				System.err.println("In '" + file.getAbsolutePath() + "'");
				ex.printStackTrace();
			}
		}
		if (reloadSettings != null) {
			this.reloadSettings = reloadSettings;
		}
		update();
	}

	public Json(final File file) {
		super(file, FileType.JSON);
		create();
		update();
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
		if (contains(key)) {
			return;
		}
		set(key, value);
	}

	/**
	 * Gets a Map by key Although used to get nested objects {@link Json}
	 *
	 * @param key Path to Map-List in JSON
	 * @return Map
	 */
	@Override
	public Map getMap(final String key) {
		String tempKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		if (!contains(tempKey)) {
			return new HashMap();
		} else {
			Object map;
			try {
				map = get(key);
			} catch (JSONException e) {
				return new HashMap<>();
			}
			if (map instanceof Map) {
				return (Map<?, ?>) fileData.get(key);
			} else if (map instanceof JSONObject) {
				return JsonUtils.jsonToMap((JSONObject) map);
			}
			throw new IllegalArgumentException("Json does not contain Map: '" + key + "'.");
		}
	}

	@Override
	public void set(final String key, final Object value) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		synchronized (this) {

			reload();

			FileData old = new FileData(fileData.toMap());


			fileData.insert(finalKey, value);


			try {
				if (old.toString().equals(fileData.toString()) && getFile().length() != 0) {
					return;
				}
				write(fileData.toJsonObject());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void write(final JSONObject object) throws IOException {
		Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
		writer.write(object.toString(3));
		writer.close();
	}

	@Override
	public <T> T getOrSetDefault(final String key, T def) {
		if (!contains(key)) {
			set(key, def);
			return def;
		} else {
			return (T) get(key);
		}
	}

	@Override
	protected void update() {
		final JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(FileUtils.createNewInputStream(file)));
		fileData = new FileData(new JSONObject(jsonTokener));
	}

	@Override
	public void remove(final String key) {
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
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			Json json = (Json) obj;
			return this.fileData.equals(json.fileData)
					&& this.pathPrefix.equals(json.pathPrefix)
					&& super.equals(json.getFlatFileInstance());
		}
	}
}