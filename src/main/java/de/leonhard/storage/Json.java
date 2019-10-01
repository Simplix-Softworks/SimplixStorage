package de.leonhard.storage;

import de.leonhard.storage.base.FileType;
import de.leonhard.storage.base.ReloadSettings;
import de.leonhard.storage.base.StorageBase;
import de.leonhard.storage.base.StorageCreator;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.JsonUtil;
import de.leonhard.storage.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

@SuppressWarnings({"Duplicates", "unused", "ResultOfMethodCallIgnored", "WeakerAccess", "unchecked"})
public class Json extends StorageCreator implements StorageBase {
	private JSONObject object;
	private File file;
	private String pathPrefix;
	private ReloadSettings reloadSettings;

	/**
	 * Creates a .json file where you can put your data in.+
	 *
	 * @param name
	 *            Name of the .json file
	 * @param path
	 *            Absolute path, where the .json file should be created.
	 */

	public Json(final String name, final String path) {

		try {
			create(path, name, FileType.JSON);

			this.file = super.file;

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException | NullPointerException e) {
				e.printStackTrace();
			}

			if (file.length() == 0) {
				object = new JSONObject();
				Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
				writer.write(object.toString(2));
				writer.close();
			}

			final JSONTokener tokener = new JSONTokener(fis);
			object = new JSONObject(tokener);

			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.err.println(
					"Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
			ex.printStackTrace();
		}

		this.reloadSettings = ReloadSettings.INTELLIGENT;
	}

	public Json(final String name, final String path, ReloadSettings reloadSettings) {

		try {
			create(path, name, FileType.JSON);

			this.file = super.file;

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException | NullPointerException e) {
				e.printStackTrace();
			}

			if (file.length() == 0) {
				object = new JSONObject();
				Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
				writer.write(object.toString(3));
				writer.close();
			}

			final JSONTokener tokener = new JSONTokener(fis);
			object = new JSONObject(tokener);

		} catch (Exception ex) {
			System.err.println(
					"Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
			ex.printStackTrace();
		}

		this.reloadSettings = reloadSettings;

	}

	public Json(final File file) {

		try {
			if (!file.exists())
				file.createNewFile();

			load(file);
			this.file = file;

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException | NullPointerException e) {
				e.printStackTrace();
			}

			if (file.length() == 0) {
				object = new JSONObject();
				Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
				writer.write(object.toString(2));
				writer.close();
			}
			this.reloadSettings = ReloadSettings.INTELLIGENT;
			JSONTokener tokener = new JSONTokener(fis);
			object = new JSONObject(tokener);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.err.println(
					"Error while creating file - Maybe wrong format - Try deleting the file " + file.getName());
			ex.printStackTrace();
		}

	}

	/**
	 * Sets a value to the json if the file doesn't already contain the value
	 * (Not mix up with Bukkit addDefault) Uses {@link JSONObject}
	 *
	 * @param key
	 *            Key to set the value
	 * @param value
	 *            Value to set
	 */

	@Override
	public void setDefault(String key, Object value) {
		if (contains(key)) {
			return;
		}
		set(key, value);
	}

	private void reload() {

		if (ReloadSettings.MANUALLY.equals(reloadSettings))
			return;
		if (ReloadSettings.INTELLIGENT.equals(reloadSettings))
			if (FileUtils.hasNotChanged(file, lastModified))
				return;

		update();
	}

	@Override
	public Object get(final String key) {

		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		return getObject(finalKey);
	}

	private Object getObject(final String key) {
		if (!has(key))
			return null;

		if (key.contains(".")) {
			return Utils.contains(key, object.toMap()) ? Utils.get(key, object.toMap()) : null;
		}
		return object.has(key) ? object.get(key) : null;
	}

	/**
	 * Gets a Map by key Although used to get nested objects {@link Json}
	 *
	 * @param key
	 *            Path to Map-List in JSON
	 * @return Map
	 */

	@Override
	public Map getMap(String key) {
		if (!contains(key))
			return new HashMap();
		key = (pathPrefix == null) ? key : pathPrefix + "." + key;
		return getMapWithoutPath(key);
	}

	private Map getMapWithoutPath(final String key) {
		reload();

		if (!has(key))
			return new HashMap<>();

		Object map;
		try {
			map = getObject(key);
		} catch (JSONException e) {
			return new HashMap<>();
		}
		if (map instanceof Map) {
			return (Map<?, ?>) object.get(key);
		} else if (map instanceof JSONObject) {
			return JsonUtil.jsonToMap((JSONObject) map);
		}
		throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
	}

	@Override
	public void set(final String key, final Object value) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		synchronized (this) {

			reload();

			if (finalKey.contains(".")) {

				JSONObject old = this.object;

				final Map map = Utils.stringToMap(finalKey, value, object.toMap());

				object = new JSONObject(map);
				try {
					if (old.toString().equals(object.toString()) && file.length() != 0)
						return;

					write(object);

				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			object.put(finalKey, value);
			try {
				Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
				writer.write(object.toString(2));
				writer.close();
			} catch (IOException e) {
				System.err.println("Couldn' t set " + finalKey + " " + value);
				e.printStackTrace();
			}
		}
	}

	public void write(final JSONObject object) throws IOException {
		Writer writer = new PrintWriter(new FileWriter(file.getAbsolutePath()));
		writer.write(object.toString(3));
		writer.close();
	}

	@Override
	public <T> T getOrSetDefault(final String path, T def) {
		if (!contains(path)) {
			set(path, def);
			return def;
		} else {
			return (T) get(path);
		}
	}

	@Override
	public void update() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException | NullPointerException e) {
			System.err.println("Exception while reading Json");
			e.printStackTrace();
		}
		final JSONTokener tokener = new JSONTokener(Objects.requireNonNull(fis));
		object = new JSONObject(tokener);
	}

	@Override
	public Set<String> getKeySet() {
		reload();
		return object.toMap().keySet();
	}

	public Set<String> getKeySet(String key) {
		reload();
		if (!contains(key))
			return new HashSet<>();
		try {
			JSONObject keys = (JSONObject) object.get(key);
			return keys.keySet();
		} catch (final Exception ex) {
			return new HashSet<>();
		}
	}

	private boolean has(final String key) {
		reload();
		if (key.contains("."))
			return Utils.contains(key, object.toMap());
		return object.has(key);
	}

	@Override
	public boolean contains(final String key) {
		String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;
		return has(finalKey);
	}

	@Override
	public void removeKey(final String key) {
		final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

		if (finalKey.contains(".")) {
			remove(key);
			return;
		}

		final Map obj = object.toMap();
		obj.remove(key);

		object = new JSONObject(obj);
		try {
			write(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void remove(final String key) {
		final String finalKey = (pathPrefix == null || pathPrefix.isEmpty()) ? key : pathPrefix + "." + key;

		if (!finalKey.contains("."))
			removeKey(key);

		final Map<String, Object> old = object.toMap();
		object = new JSONObject(Utils.remove(old, finalKey));
		try {
			write(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}
}