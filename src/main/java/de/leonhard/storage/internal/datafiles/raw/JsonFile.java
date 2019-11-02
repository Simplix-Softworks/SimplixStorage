package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.datafiles.section.JsonSection;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.JsonUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Cleanup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Class to manager Json-Type Files
 */
@SuppressWarnings("unused")
public class JsonFile extends FlatFile {

	public JsonFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final Reload reloadSetting, @Nullable final DataType dataType) {
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

		final JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(FileUtils.createNewInputStream(file)));
		fileData = new FileData(new JSONObject(jsonTokener));
		this.lastLoaded = System.currentTimeMillis();
	}


	@Override
	public void reload() {
		final JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(FileUtils.createNewInputStream(file)));
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
	public Map getMap(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		if (!hasKey(key)) {
			return new HashMap();
		} else {
			return getMapWithoutPath(key);
		}
	}

	private Map getMapWithoutPath(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();

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

	private Object getObject(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		if (!hasKey(key)) {
			return null;
		}

		/*if (key.contains(".")) {
			return new FileData(fileData.toMap()).containsKey(key) ? new FileData(fileData.toMap()).get(key) : null;
		}*/
		return fileData.containsKey(key) ? fileData.get(key) : null;
	}

	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		if (insert(key, value)) {
			try {
				write(new JSONObject(fileData.toMap()));
			} catch (IOException e) {
				System.err.println("Error while writing to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	private void write(final JSONObject object) throws IOException {
		@Cleanup Writer writer = new PrintWriter(new FileWriter(getFile().getAbsolutePath()));
		writer.write(object.toString(3));
	}

	@Override
	public Object get(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		return getObject(key);
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (fileData.containsKey(key)) {
			fileData.remove(key);
			try {
				write(fileData.toJsonObject());
			} catch (IOException e) {
				System.err.println("Could not write to '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public JsonSection getSection(@NotNull final String sectionKey) {
		return new JsonSection(this, sectionKey);
	}

	protected final JsonFile getJsonFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			JsonFile json = (JsonFile) obj;
			return super.equals(json.getFlatFileInstance());
		}
	}
}