package de.leonhard.storage.internal.data;

import de.leonhard.storage.internal.utils.JsonUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


/**
 * Class to handle the Nested HashMaps used to cache the Data read from the Files
 */
@SuppressWarnings("unused")
public class FileData {

	private final Map<String, Object> localMap;

	protected FileData(final @Nullable Map<String, Object> map) {
		this.localMap = map != null ? (map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map)) : new HashMap<>();
	}

	protected FileData(final @NotNull JSONObject jsonObject) {
		this.localMap = new HashMap<>(Objects.notNull(jsonObject, "JsonObject must not be null").toMap());
	}


	/**
	 * Reload the contents of FileData.
	 *
	 * @param map the Contents to be inserted.
	 */
	public synchronized void loadData(final @Nullable Map<String, Object> map) {
		this.localMap.clear();
		if (map != null) {
			this.localMap.putAll(map);
		}
	}

	public synchronized void loadData(final @Nullable JSONObject jsonObject) {
		this.localMap.clear();
		if (jsonObject != null) {
			this.localMap.putAll(jsonObject.toMap());
		}
	}


	/**
	 * Method to assign a value to a key.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to the key.
	 */
	public synchronized void insert(final @NotNull String key, final @Nullable Object value) {
		if (value == null) {
			this.remove(key);
		} else {
			final String[] parts = key.split("\\.");
			//noinspection unchecked
			this.localMap.put(parts[0],
							  this.localMap.containsKey(parts[0])
							  && this.localMap.get(parts[0]) instanceof Map
							  ? this.insert((Map<String, Object>) this.localMap.get(parts[0]), parts, value, 1)
							  : this.insert(new HashMap<>(), parts, value, 1));
		}
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists.
	 *
	 * @param key the key to be removed from the map.
	 */
	public synchronized void remove(final @NotNull String key) {
		if (this.containsKey(key)) {
			final String[] parts = key.split("\\.");
			this.removeKey(this.localMap, parts, 0);
		}
	}

	/**
	 * Check whether the map contains a certain key.
	 *
	 * @param key the key to be looked for.
	 * @return true if the key exists, otherwise false.
	 */
	public boolean containsKey(final @NotNull String key) {
		String[] parts = key.split("\\.");
		return this.containsKey(this.localMap, parts, 0);
	}

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	public Set<String> keySet() {
		return this.keySet(localMap);
	}

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the Block.
	 * @return the keySet of all sublayers of the given key or null if the key does not exist (Format: key.subkey).
	 */
	public Set<String> keySet(final @NotNull String key) {
		//noinspection unchecked
		return this.get(key) instanceof Map ? this.keySet((Map<String, Object>) this.get(key)) : null;
	}

	/**
	 * Method to get the object assign to a key from a FileData Object.
	 *
	 * @param key the key to look for.
	 * @return the value assigned to the given key or null if the key does not exist.
	 */
	public Object get(final @NotNull String key) {
		final String[] parts = key.split("\\.");
		return this.get(this.localMap, parts, 0);
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	public Set<String> blockKeySet() {
		return this.localMap.keySet();
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or null if the key does not exist.
	 */
	public Set<String> blockKeySet(final @NotNull String key) {
		//noinspection unchecked
		return this.get(key) instanceof Map ? ((Map<String, Object>) this.get(key)).keySet() : null;
	}

	/**
	 * Get the size of a single layer of the map.
	 *
	 * @return the size of the top layer of localMap.
	 */
	public int blockSize() {
		return this.localMap.size();
	}

	/**
	 * get the size of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the size of the given layer or 0 if the key does not exist.
	 */
	public int blockSize(final @NotNull String key) {
		return this.get(key) instanceof Map ? ((Map) this.get(key)).size() : -1;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public int size() {
		return this.size(localMap);
	}

	/**
	 * Convert FileData to a JsonObject.
	 *
	 * @return JsonObject from localMap.
	 */
	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(this.localMap);
	}

	/**
	 * Convert FileData to a nested HashMap.
	 *
	 * @return localMap.
	 */
	public Map<String, Object> toMap() {
		return this.localMap;
	}

	/**
	 * get the size of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist.
	 */
	public int size(final @NotNull String key) {
		//noinspection unchecked
		return this.containsKey(key) ? this.size((Map<String, Object>) this.get(key)) : -1;
	}

	/**
	 * Clear the contents of this FileData.
	 */
	public void clear() {
		this.localMap.clear();
	}


	private Map<String, Object> removeKey(Map<String, Object> map, String[] key, int id) {
		Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				tempMap.remove(key[id]);
				return tempMap;
			} else {
				//noinspection unchecked
				map.put(key[id], this.removeKey((Map<String, Object>) map.get(key[id]), key, id + 1));
				//noinspection unchecked
				if (((Map<String, Object>) map.get(key[id])).isEmpty()) {
					map.remove(key[id]);
				}
				return map;
			}
		} else {
			return null;
		}
	}

	private boolean containsKey(final Map<String, Object> map, final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
				return this.containsKey(tempMap, key, id + 1);
			} else {
				return false;
			}
		} else {
			return map.containsKey(key[id]);
		}
	}

	private Object get(final Map<String, Object> map, final String[] key, final int id) {
		if (id < key.length - 1) {
			if (map.get(key[id]) instanceof Map) {
				//noinspection unchecked
				Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
				return this.get(tempMap, key, id + 1);
			} else {
				return null;
			}
		} else {
			return map.get(key[id]);
		}
	}

	private synchronized Object insert(final Map<String, Object> map, final String[] key, final Object value, final int id) {
		if (id < key.length) {
			Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
			//noinspection unchecked
			Map<String, Object> childMap =
					map.containsKey(key[id])
					&& map.get(key[id]) instanceof Map
					? (Map<String, Object>) map.get(key[id])
					: (map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map));
			tempMap.put(key[id], this.insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}

	private Set<String> keySet(final Map<String, Object> map) {
		Set<String> localSet = new HashSet<>();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<String, Object>) map.get(key))) {
					localSet.add(key + "." + tempKey);
				}
			} else {
				localSet.add(key);
			}
		}
		return localSet;
	}

	private int size(final Map<String, Object> map) {
		int size = map.size();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {
				//noinspection unchecked
				size += this.size((Map<String, Object>) map.get(key));
			}
		}
		return size;
	}

	@Override
	public int hashCode() {
		return this.localMap.hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FileData fileData = (FileData) obj;
			return this.localMap.equals(fileData.localMap);
		}
	}

	@Override
	public String toString() {
		return this.localMap.toString();
	}
}