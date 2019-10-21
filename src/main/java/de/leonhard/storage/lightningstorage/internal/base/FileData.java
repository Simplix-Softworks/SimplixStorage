package de.leonhard.storage.lightningstorage.internal.base;

import de.leonhard.storage.lightningstorage.utils.JsonUtils;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;


@SuppressWarnings({"unused", "WeakerAccess"})
public class FileData {

	private final Map<String, Object> localMap;

	/**
	 * Creates an empty FileData Object.
	 */
	public FileData() {
		this.localMap = new HashMap<>();
	}

	/**
	 * @param map the Map to be processed.
	 */
	public FileData(final Map<String, Object> map) {
		this.localMap = map instanceof LinkedHashMap ? new LinkedHashMap<>(map) : new HashMap<>(map);
	}

	/**
	 * @param jsonObject the JsonObject to be processed.
	 */
	public FileData(final JSONObject jsonObject) {
		this.localMap = new HashMap<>(jsonObject.toMap());
	}


	/**
	 * Check whether the map contains a certain key.
	 *
	 * @param key the key to be looked for.
	 * @return true if the key exists, otherwise false.
	 */
	public boolean containsKey(final String key) {
		String[] parts = key.split("\\.");
		return containsKey(localMap, parts, 0);
	}

	/**
	 * Method to get the object assign to a key from a FileData Object.
	 *
	 * @param key the key to look for.
	 * @return the value assigned to the given key or null if the key does not exist.
	 */
	public Object get(final String key) {
		final String[] parts = key.split("\\.");
		return get(localMap, parts, 0);
	}

	/**
	 * Method to assign a value to a key.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to the key.
	 */
	public synchronized void insert(final String key, final Object value) {
		final String[] parts = key.split("\\.");
		//noinspection unchecked
		localMap.put(parts[0],
					 localMap.containsKey(parts[0])
					 && localMap.get(parts[0]) instanceof Map
					 ? insert((Map<String, Object>) localMap.get(parts[0]), parts, value, 1)
					 : insert(new HashMap<>(), parts, value, 1));
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
	public Set<String> keySet(final String key) {
		//noinspection unchecked
		return get(key) instanceof Map ? keySet((Map<String, Object>) get(key)) : new HashSet<>();
	}

	/**
	 * Remove a key with its assigned value from the map if given key exists.
	 *
	 * @param key the key to be removed from the map.
	 */
	public synchronized void remove(final String key) {
		if (containsKey(key)) {
			final String[] parts = key.split("\\.");
			remove(localMap, parts, 0);
		}
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
	public Set<String> singleLayerKeySet(final String key) {
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
	public int singleLayerSize(final String key) {
		return get(key) instanceof Map ? ((Map) get(key)).size() : 0;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public int size() {
		return localMap.size();
	}

	/**
	 * get the size of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist.
	 */
	public int size(final String key) {
		return localMap.size();
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
			Map<String, Object> tempMap = new HashMap<>(map);
			//noinspection unchecked
			Map<String, Object> childMap =
					map.containsKey(key[id])
					&& map.get(key[id]) instanceof Map
					? (Map<String, Object>) map.get(key[id])
					: new HashMap<>();
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

	private synchronized void remove(final Map<String, Object> map, final String[] key, final int id) {
		Map tempMap = map;
		for (int i = 0; i < key.length - (1 + id); i++) {
			if (tempMap.containsKey(key[i]) && tempMap.get(key[i]) instanceof Map) {
				tempMap = (Map) tempMap.get(key[i]);
			}
		}
		if (tempMap.keySet().size() <= 1) {
			map.remove(key[key.length - (1 + id)]);
			remove(map, key, id + 1);
		}
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

	/**
	 * convert FileData to string.
	 *
	 * @return the String value of localMap.
	 */
	@Override
	public String toString() {
		return this.localMap.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			FileData fileData = (FileData) obj;
			return this.localMap.equals(fileData.localMap);
		}
	}


	public enum Type {

		SORTED,
		STANDARD,
		AUTOMATIC;

		public Map<String, Object> getNewDataMap(@NotNull final FlatFile.ConfigSetting configSetting, @Nullable final Map<String, Object> map) {
			if (this == SORTED) {
				return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
			} else if (this == STANDARD) {
				return map == null ? new HashMap<>() : new HashMap<>(map);
			} else if (this == AUTOMATIC) {
				if (configSetting == FlatFile.ConfigSetting.SKIP_COMMENTS) {
					return map == null ? new HashMap<>() : new HashMap<>(map);
				} else if (configSetting == FlatFile.ConfigSetting.PRESERVE_COMMENTS) {
					return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
				} else {
					throw new IllegalStateException("Illegal ConfigSetting");
				}
			} else {
				throw new IllegalStateException("Illegal DataType");
			}
		}

		public List<String> getNewDataList(@NotNull final FlatFile.ConfigSetting configSetting, @Nullable final List<String> list) {
			if (this == SORTED) {
				return list == null ? new LinkedList<>() : new LinkedList<>(list);
			} else if (this == STANDARD) {
				return list == null ? new ArrayList<>() : new ArrayList<>(list);
			} else if (this == AUTOMATIC) {
				if (configSetting == FlatFile.ConfigSetting.SKIP_COMMENTS) {
					return list == null ? new ArrayList<>() : new ArrayList<>(list);
				} else if (configSetting == FlatFile.ConfigSetting.PRESERVE_COMMENTS) {
					return list == null ? new LinkedList<>() : new LinkedList<>(list);
				} else {
					throw new IllegalStateException("Illegal ConfigSetting");
				}
			} else {
				throw new IllegalStateException("Illegal DataType");
			}
		}
	}
}