package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.utils.JsonUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An extended HashMap, to easily use the nested HashMaps created by reading the Configuration files.
 */
@SuppressWarnings("unused")
public class FileData {

	private final HashMap<String, Object> localMap;

	/**
	 * Constructor for FileData
	 *
	 * @param map the the class utilizes.
	 */
	public FileData(final Map<String, Object> map) {
		localMap = new HashMap<>(map);
	}

	public FileData() {
		localMap = new HashMap<>();
	}

	public FileData(final JSONObject jsonObject) {
		localMap = new HashMap<>(jsonObject.toMap());
	}


	/**
	 * Method to get the object assign to a key from a FileData Object.
	 *
	 * @param key the key to look for.
	 * @return the value assigned to the given key or null if the key does not exist.
	 */
	public synchronized Object get(final String key) {
		final String[] parts = key.split("\\.");
		return get(localMap, parts, 0);
	}

	private synchronized Object get(final Map<String, Object> map, final String[] key, final int id) {
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


	/**
	 * Method to assign a value to a key.
	 *
	 * @param key   the key to be used.
	 * @param value the value to be assigned to the key.
	 */
	public synchronized void insert(final String key, final Object value) {
		final String[] parts = key.split("\\.");
		localMap.put(parts[0], insert(localMap, parts, value, 1));
	}

	private synchronized Object insert(final Map<String, Object> map, final String[] key, final Object value, final int id) {
		if (id < key.length) {
			Map<String, Object> tempMap = new HashMap<>(map);
			//noinspection unchecked
			Map<String, Object> childMap = map.containsKey(key[id]) ? (map.get(key[id]) instanceof Map ? (Map<String, Object>) map.get(key[id]) : new HashMap<>()) : new HashMap<>();
			tempMap.put(key[id], insert(childMap, key, value, id + 1));
			return tempMap;
		} else {
			return value;
		}
	}


	/**
	 * Check whether the map contains a certain key.
	 *
	 * @param key the key to be looked for.
	 * @return true if the key exists, otherwise false.
	 */
	public synchronized boolean containsKey(final String key) {
		String[] parts = key.split("\\.");
		return containsKey(localMap, parts, 0);
	}

	private synchronized boolean containsKey(final Map<String, Object> map, final String[] key, final int id) {
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


	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @return the keySet of the top layer of localMap.
	 */
	public synchronized Set<String> singleLayerKeySet() {
		return localMap.keySet();
	}

	/**
	 * get the keySet of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the keySet of the given layer or an empty set if the key does not exist.
	 */
	public synchronized Set<String> singleLayerKeySet(final String key) {
		//noinspection unchecked
		return get(key) instanceof Map ? ((Map<String, Object>) get(key)).keySet() : new HashSet<>();
	}


	/**
	 * get the keySet of all layers of the map combined.
	 *
	 * @return the keySet of all layers of localMap combined (Format: key.subkey).
	 */
	public synchronized Set<String> keySet() {
		return keySet(localMap);
	}

	/**
	 * get the keySet of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the keySet of all sublayers of the given key or an empty set if the key does not exist (Format: key.subkey).
	 */
	public synchronized Set<String> keySet(final String key) {
		//noinspection unchecked
		return get(key) instanceof Map ? keySet((Map<String, Object>) get(key)) : new HashSet<>();
	}

	private synchronized Set<String> keySet(final Map<String, Object> map) {
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


	/**
	 * Get the size of a single layer of the map.
	 *
	 * @return the size of the top layer of localMap.
	 */
	public synchronized int singleLayerSize() {
		return localMap.size();
	}

	/**
	 * get the size of a single layer of the map.
	 *
	 * @param key the key of the layer.
	 * @return the size of the given layer or 0 if the key does not exist.
	 */
	public synchronized int singleLayerSize(final String key) {
		return get(key) instanceof Map ? ((Map) get(key)).size() : 0;
	}

	/**
	 * Get the size of the local map.
	 *
	 * @return the size of all layers of localMap combined.
	 */
	public synchronized int size() {
		return localMap.size();
	}

	/**
	 * get the size of all sublayers of the given key combined.
	 *
	 * @param key the key of the layer
	 * @return the size of all sublayers of the given key or 0 if the key does not exist.
	 */
	public synchronized int size(final String key) {
		return localMap.size();
	}

	private synchronized int size(final Map<String, Object> map) {
		int size = map.size();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {
				//noinspection unchecked
				size += size((Map<String, Object>) map.get(key));
			}
		}
		return size;
	}


	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(localMap);
	}


	/**
	 * Get localMap.
	 *
	 * @return localMap.
	 * @throws IllegalStateException() Thrown if localMap is null.
	 */
	public synchronized Map<String, Object> toMap() {
		if (localMap == null) {
			throw new IllegalStateException("Couldn't get null");
		} else {
			return localMap;
		}
	}

	@Override
	public synchronized boolean equals(final Object obj) {
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
	public synchronized int hashCode() {
		return this.localMap.hashCode();
	}

	/**
	 * convert FileData to string.
	 *
	 * @return the String value of localMap.
	 */
	@Override
	public synchronized String toString() {
		return this.localMap.toString();
	}
}