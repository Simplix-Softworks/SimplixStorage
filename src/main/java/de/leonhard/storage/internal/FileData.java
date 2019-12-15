package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.util.JsonUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An extended HashMap, to easily process
 * the nested HashMaps created by reading the Configuration files.
 */
@SuppressWarnings("unchecked")
public class FileData {
    private final Map<String, Object> localMap;

    public FileData(Map<String, Object> map, DataType dataType) {
        localMap = dataType.getMapImplementation();

        localMap.putAll(map);
    }

    public FileData(JSONObject jsonObject) {
        localMap = new HashMap<>(jsonObject.toMap());
    }

    public FileData(JSONObject jsonObject, DataType dataType) {
        localMap = dataType.getMapImplementation();
        localMap.putAll(jsonObject.toMap());
    }

    public void clear() {
        localMap.clear();
    }

    public void loadData(final Map<String, Object> map) {
        clear();

        if (map != null) {
            localMap.putAll(map);
        }
    }

    /**
     * Method to get the object assign to a key from a FileData Object.
     *
     * @param key the key to look for.
     * @return the value assigned to the given key or null if the key does not exist.
     */
    public Object get(String key) {
        String[] parts = key.split("\\.");
        return get(localMap, parts, 0);
    }

    private Object get(Map<String, Object> map, String[] key, int id) {
        if (id < key.length - 1) {
            if (map.get(key[id]) instanceof Map) {
                Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
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
    public synchronized void insert(String key, Object value) {
        String[] parts = key.split("\\.");
        localMap.put(parts[0],
                localMap.containsKey(parts[0])
                        && localMap.get(parts[0]) instanceof Map
                        ? insert((Map<String, Object>) localMap.get(parts[0]), parts, value, 1)
                        : insert(new HashMap<>(), parts, value, 1));
    }

    private Object insert(Map<String, Object> map, String[] key, Object value, int id) {
        if (id < key.length) {
            Map<String, Object> tempMap = new HashMap<>(map);
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
    public boolean containsKey(String key) {
        String[] parts = key.split("\\.");
        return containsKey(localMap, parts, 0);
    }

    private boolean containsKey(Map<String, Object> map, String[] key, int id) {
        if (id < key.length - 1) {
            if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
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
    public synchronized void remove(String key) {
        if (containsKey(key)) {
            String[] parts = key.split("\\.");
            remove(localMap, parts, 0);
        }
    }

    private synchronized void remove(Map<String, Object> map, String[] key, int id) {
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
    public Set<String> singleLayerKeySet(String key) {
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
    public Set<String> keySet(String key) {
        return get(key) instanceof Map ? keySet((Map<String, Object>) get(key)) : new HashSet<>();
    }

    private Set<String> keySet(Map<String, Object> map) {
        Set<String> localSet = new HashSet<>();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
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
    public int singleLayerSize(String key) {
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
    public int size(String key) {
        return localMap.size();
    }

    private int size(Map<String, Object> map) {
        int size = map.size();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                size += size((Map<String, Object>) map.get(key));
            }
        }
        return size;
    }

    // ----------------------------------------------------------------------------------------------------
    // Utility functions
    // ----------------------------------------------------------------------------------------------------

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

    // ----------------------------------------------------------------------------------------------------
    // Overridden methods form Object
    // ----------------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return localMap.hashCode();
    }

    @Override
    public String toString() {
        return localMap.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final FileData fileData = (FileData) obj;
            return localMap.equals(fileData.localMap);
        }
    }
}