package de.leonhard.storage.internal;

import de.leonhard.storage.utils.JsonUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An extended HashMap, to easily process the nested HashMaps created by reading the Configuration files.
 */
@SuppressWarnings("unused")
public class FileData {

	private final HashMap<String, Object> localMap;

	public FileData() {
		this.localMap = new HashMap<>();
	}

	public FileData(final Map<String, Object> map) {
		this.localMap = new HashMap<>(map);
	}

	public FileData(final JSONObject jsonObject) {
		this.localMap = new HashMap<>(jsonObject.toMap());
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


	public Map<String, Object> toMap() {
		if (localMap != null) {
			return localMap;
		} else {
			return new HashMap<>();
		}
	}

	public JSONObject toJsonObject() {
		return JsonUtils.getJsonFromMap(localMap);
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
}