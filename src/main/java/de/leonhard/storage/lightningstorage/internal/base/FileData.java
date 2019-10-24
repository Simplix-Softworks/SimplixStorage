package de.leonhard.storage.lightningstorage.internal.base;

import de.leonhard.storage.lightningstorage.utils.JsonUtils;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


@SuppressWarnings({"unused", "WeakerAccess"})
public class FileData {

	private final Map<String, Object> localMap;

	public FileData(@Nullable final Map<String, Object> map) {
		this.localMap = map != null ? (map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map)) : new HashMap<>();
	}

	public FileData(@NotNull final JSONObject jsonObject) {
		this.localMap = new HashMap<>(jsonObject.toMap());
	}


	/**
	 * Method to assign a value to a key.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to the key.
	 */
	public synchronized void insert(@NotNull final String key, @Nullable final Object value) {
		if (value == null) {
			remove(key);
		} else {
			final String[] parts = key.split("\\.");
			//noinspection unchecked
			localMap.put(parts[0],
						 localMap.containsKey(parts[0])
						 && localMap.get(parts[0]) instanceof Map
						 ? insert((Map<String, Object>) localMap.get(parts[0]), parts, value, 1)
						 : insert(new HashMap<>(), parts, value, 1));
		}
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists.
	 *
	 * @param key the key to be removed from the map.
	 */
	public synchronized void remove(@NotNull final String key) {
		if (containsKey(key)) {
			final String[] parts = key.split("\\.");
			removeKey(this.localMap, parts, 0);
		}
	}

	/**
	 * Check whether the map contains a certain key.
	 *
	 * @param key the key to be looked for.
	 * @return true if the key exists, otherwise false.
	 */
	public boolean containsKey(@NotNull final String key) {
		String[] parts = key.split("\\.");
		return containsKey(localMap, parts, 0);
	}

	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	public Set<String> keySet() {
		return keySet(localMap);
	}

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the keySet of all sublayers of the given key or an empty set if the key does not exist (Format: key.subkey).
	 */
	public Set<String> keySet(@NotNull final String key) {
		//noinspection unchecked
		return get(key) instanceof Map ? keySet((Map<String, Object>) get(key)) : new HashSet<>();
	}

	/**
	 * Method to get the object assign to a key from a FileData Object.
	 *
	 * @param key the key to look for.
	 * @return the value assigned to the given key or null if the key does not exist.
	 */
	public Object get(@NotNull final String key) {
		final String[] parts = key.split("\\.");
		return get(localMap, parts, 0);
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	public Set<String> singleLayerKeySet() {
		return localMap.keySet();
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or an empty set if the key does not exist.
	 */
	public Set<String> singleLayerKeySet(@NotNull final String key) {
		//noinspection unchecked
		return get(key) instanceof Map ? ((Map<String, Object>) get(key)).keySet() : new HashSet<>();
	}

	/**
	 * Get the size of a single layer of the map.
	 *
	 * @return the size of the top layer of localMap.
	 */
	public int singleLayerSize() {
		return localMap.size();
	}

	/**
	 * get the size of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the size of the given layer or 0 if the key does not exist.
	 */
	public int singleLayerSize(@NotNull final String key) {
		return get(key) instanceof Map ? ((Map) get(key)).size() : 0;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public int size() {
		return size(localMap);
	}

	/**
	 * Convert FileData to a JsonObject.
	 *
	 * @return JsonObject from localMap.
	 */
	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(localMap);
	}

	/**
	 * Convert FileData to a nested HashMap.
	 *
	 * @return localMap.
	 */
	public Map<String, Object> toMap() {
		if (localMap != null) {
			return localMap;
		} else {
			return new HashMap<>();
		}
	}

	/**
	 * get the size of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist.
	 */
	public int size(@NotNull final String key) {
		//noinspection unchecked
		return containsKey(key) ? size((Map<String, Object>) get(key)) : 0;
	}


	private Map<String, Object> removeKey(Map<String, Object> map, String[] key, int id) {
		Map<String, Object> tempMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
		if (id < key.length) {
			if (id == key.length - 1) {
				tempMap.remove(key[id]);
				return tempMap;
			} else {
				//noinspection unchecked
				map.put(key[id], removeKey((Map<String, Object>) map.get(key[id]), key, id + 1));
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
				return containsKey(tempMap, key, id + 1);
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
				@SuppressWarnings("unchecked") Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
				return get(tempMap, key, id + 1);
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
			tempMap.put(key[id], insert(childMap, key, value, id + 1));
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
				for (String tempKey : keySet((Map<String, Object>) map.get(key))) {
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
				size += size((Map<String, Object>) map.get(key));
			}
		}
		return size;
	}

	@Override
	public int hashCode() {
		return this.localMap.hashCode();
	}

	@Override
	public String toString() {
		return this.localMap.toString();
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FileData fileData = (FileData) obj;
			return this.localMap.equals(fileData.localMap);
		}
	}

	/**
	 * an Enum defining how the Data should be stored.
	 */
	public enum Type {

		/**
		 * The Data is stored in a LinkedHashMap.
		 */
		SORTED,
		/**
		 * The Data is stored in a HashMap.
		 */
		STANDARD,
		/**
		 * the Storage type depends on the ConfigSetting(HashMap for SKIP_COMMENTS, LinkedHashMap for PRESERVE_COMMENTS)
		 */
		AUTOMATIC
	}
}