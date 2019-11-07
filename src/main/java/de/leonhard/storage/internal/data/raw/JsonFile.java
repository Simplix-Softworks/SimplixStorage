package de.leonhard.storage.internal.data.raw;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.data.section.JsonSection;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.JsonUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Class to manage Json-Type Files
 */
@SuppressWarnings("unused")
public class JsonFile extends FlatFile {

	protected JsonFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final @Nullable DataType dataType) {
		super(file, FileType.JSON);

		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (dataType != null) {
			setDataType(dataType);
		} else {
			setDataType(DataType.STANDARD);
		}
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}

		final JSONTokener jsonTokener = new JSONTokener(Objects.notNull(FileUtils.createNewInputStream(file), "InputStream must not be null"));
		fileData = new LocalFileData(new JSONObject(jsonTokener));
		this.lastLoaded = System.currentTimeMillis();
	}


	@Override
	public void reload() {
		final JSONTokener jsonTokener = new JSONTokener(Objects.notNull(FileUtils.createNewInputStream(file), "InputStream must not be null"));
		fileData.loadData(new JSONObject(jsonTokener));
		this.lastLoaded = System.currentTimeMillis();
	}

	/**
	 * Gets a Map by key Although used to get nested objects {@link JsonFile}
	 *
	 * @param key Path to Map-List in JSON
	 * @return Map
	 */

	@Override
	public Map getMap(final @NotNull String key) {
		if (!hasKey(key)) {
			return new HashMap();
		} else {
			return getMapWithoutPath(key);
		}
	}

	@Override
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		update();
		return this.fileData.get(key);
	}

	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
		if (this.insert(key, value)) {
			try {
				write(new JSONObject(fileData.toMap()));
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
			try {
				write(new JSONObject(fileData.toMap()));
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
			try {
				write(new JSONObject(fileData.toMap()));
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		update();

		fileData.remove(key);

		try {
			write(fileData.toJsonObject());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String key : keys) {
			fileData.remove(key);
		}

		try {
			write(fileData.toJsonObject());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		update();

		for (String tempKey : keys) {
			fileData.remove(key + "." + tempKey);
		}

		try {
			write(fileData.toJsonObject());
		} catch (IOException e) {
			System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public JsonSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	protected final JsonFile getJsonFileInstance() {
		return this;
	}

	private Map getMapWithoutPath(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		update();

		if (!hasKey(key)) {
			return new HashMap<>();
		}

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
		throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
	}

	private void write(final JSONObject object) throws IOException {
		@Cleanup Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
		writer.write(object.toString(3));
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonFile json = (JsonFile) obj;
			return super.equals(json.getFlatFileInstance());
		}
	}


	private static class LocalSection extends JsonSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull JsonFile jsonFile) {
			super(sectionKey, jsonFile);
		}
	}
}