package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.util.JsonUtils;
import lombok.Synchronized;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;

/**
 * An extended HashMap, to easily process the nested HashMaps created by reading the Configuration
 * files.
 */
@SuppressWarnings({"unchecked", "unused"})
public class FileData {

    private final @NotNull Map<String, Object> localMap;

    public FileData(final @NotNull Map<String, Object> map,
                    final @NotNull DataType dataType) {
        this.localMap = dataType.getMapImplementation();
        this.localMap.putAll(map);
    }

    public FileData(final @NotNull JSONObject jsonObject) {
        this.localMap = new HashMap<>(jsonObject.toMap());
    }

    public FileData(final @NotNull JSONObject jsonObject,
                    final @NotNull DataType dataType) {
        this.localMap = dataType.getMapImplementation();
        this.localMap.putAll(jsonObject.toMap());
    }

    public void clear() {
        this.localMap.clear();
    }

    /**
     * Loads data from a map clears our current data before
     *
     * @param map Map to load data from
     */
    public void loadData(final @Nullable Map<String, Object> map) {
        clear();

        if (map != null) {
            this.localMap.putAll(map);
        }
    }

    /**
     * Method to get the object assign to a key from a FileData Object.
     *
     * @param key the key to look for.
     * @return the value assigned to the given key or null if the key does not exist.
     */
    public @Nullable Object get(final @NotNull String key) {
        val parts = key.split("\\.");
        return get(this.localMap, parts, 0);
    }

    private @Nullable Object get(final @NotNull Map<String, Object> map,
                                 final String @NotNull [] key,
                                 final int id) {
        if (id < key.length - 1) {
            if (map.get(key[id]) instanceof Map) {
                val tempMap = (Map<String, Object>) map.get(key[id]);
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
    public synchronized void insert(final @NotNull String key,
                                    final Object value) {
        val parts = key.split("\\.");
        this.localMap.put(parts[0], this.localMap.containsKey(parts[0]) && this.localMap.get(parts[0]) instanceof Map
                ? insert((Map<String, Object>) this.localMap.get(parts[0]), parts, value, 1)
                : insert(new HashMap<>(), parts, value, 1));
    }

    private Object insert(final @NotNull Map<String, Object> map,
                          final String @NotNull [] key,
                          final Object value,
                          final int id) {
        if (id < key.length) {
            final Map<String, Object> tempMap = new HashMap<>(map);
            final Map<String, Object> childMap = map.containsKey(key[id]) && map.get(key[id]) instanceof Map ? (Map<String, Object>) map.get(key[id]) : new HashMap<>();

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
    public boolean containsKey(final @NotNull String key) {
        val parts = key.split("\\.");
        return containsKey(this.localMap, parts, 0);
    }

    private boolean containsKey(final @NotNull Map<String, Object> map,
                                final String @NotNull [] key,
                                final int id) {
        if (id < key.length - 1) {
            if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
                val tempMap = (Map<String, Object>) map.get(key[id]);
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
    @Synchronized
    public void remove(final @NotNull String key) {
        if (containsKey(key)) {
            val parts = key.split("\\.");
            remove(parts);
        }
    }

    private void remove(final @NotNull String @NotNull [] key) {
        if (key.length == 1) {
            this.localMap.remove(key[0]);
        } else {
            val tempValue = this.localMap.get(key[0]);

            if (tempValue instanceof Map) {
                //noinspection unchecked
                this.localMap.put(key[0], this.remove((Map<String, Object>) tempValue, key, 1));

                if (((Map<Object, Object>) this.localMap.get(key[0])).isEmpty()) {
                    this.localMap.remove(key[0]);
                }
            }
        }
    }

    private @NotNull Map<String, Object> remove(final @NotNull Map<String, Object> map, final String @NotNull [] key, final int keyIndex) {
        if (keyIndex < key.length - 1) {
            val tempValue = map.get(key[keyIndex]);
            if (tempValue instanceof Map) {
                //noinspection unchecked
                map.put(key[keyIndex], this.remove((Map<String, Object>) tempValue, key, keyIndex + 1));
                if (((Map<Object, Object>) map.get(key[keyIndex])).isEmpty()) {
                    map.remove(key[keyIndex]);
                }
            }
        } else {
            map.remove(key[keyIndex]);
        }
        return map;
    }

    /**
     * get the keySet of a single layer of the map.
     *
     * @return the keySet of the top layer of localMap.
     */
    public @NotNull Set<String> singleLayerKeySet() {
        return this.localMap.keySet();
    }

    /**
     * get the keySet of a single layer of the map.
     *
     * @param key the key of the layer.
     * @return the keySet of the given layer or an empty set if the key does not exist.
     */
    public @NotNull Set<String> singleLayerKeySet(final @NotNull String key) {
        return get(key) instanceof Map ? ((Map<String, Object>) Objects.requireNonNull(get(key))).keySet()
                : new HashSet<>();
    }

    /**
     * get the keySet of all layers of the map combined.
     *
     * @return the keySet of all layers of localMap combined (Format: key.subkey).
     */
    public @NotNull Set<String> keySet() {
        return multiLayerKeySet(this.localMap);
    }

    public @NotNull Set<Map.Entry<String, Object>> entrySet() {
        return multiLayerEntrySet(this.localMap);
    }

    public @NotNull Set<Map.Entry<String, Object>> singleLayerEntrySet() {
        return this.localMap.entrySet();
    }

    /**
     * get the keySet of all sublayers of the given key combined.
     *
     * @param key the key of the layer
     * @return the keySet of all sublayers of the given key or an empty set if the key does not exist
     * (Format: key.subkey).
     */
    public @NotNull Set<String> keySet(final @NotNull String key) {
        return get(key) instanceof Map
                ? multiLayerKeySet((Map<String, Object>) Objects.requireNonNull(get(key)))
                : new HashSet<>();
    }

    /**
     * Private helper method to get the key set of an map containing maps recursively
     */
    private @NotNull Set<String> multiLayerKeySet(final @NotNull Map<String, Object> map) {
        final Set<String> out = new HashSet<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            val key = entry.getKey();
            if (entry.getValue() instanceof Map) {
                for (val tempKey : multiLayerKeySet((Map<String, Object>) entry.getValue())) {
                    out.add(key + "." + tempKey);
                }
            } else {
                out.add(key);
            }
        }

        return out;
    }

    private @NotNull Set<Map.Entry<String, Object>> multiLayerEntrySet(final @NotNull Map<String, Object> map) {
        final Set<Map.Entry<String, Object>> out = new HashSet<>();

        for (val entry : map.entrySet()) {
            if (map.get(entry.getKey()) instanceof Map) {
                for (val tempKey : multiLayerKeySet((Map<String, Object>) map.get(entry.getKey()))) {
                    out.add(new SimpleEntry<>(entry.getKey() + "." + tempKey, entry.getValue()));
                }
            } else {
                out.add(entry);
            }
        }

        return out;
    }

    /**
     * Get the size of a single layer of the map.
     *
     * @return the size of the top layer of localMap.
     */
    public int singleLayerSize() {
        return this.localMap.size();
    }

    /**
     * get the size of a single layer of the map.
     *
     * @param key the key of the layer.
     * @return the size of the given layer or 0 if the key does not exist.
     */
    public int singleLayerSize(final @NotNull String key) {
        return get(key) instanceof Map ? ((Map<?, ?>) Objects.requireNonNull(get(key))).size() : 0;
    }

    /**
     * Get the size of the local map.
     *
     * @return the size of all layers of localMap combined.
     */
    public int size() {
        return this.localMap.size();
    }

    /**
     * get the size of all sublayers of the given key combined.
     *
     * @param key the key of the layer
     * @return the size of all sublayers of the given key or 0 if the key does not exist.
     */
    public int size(final String key) {
        return this.localMap.size();
    }

    public void putAll(final @NotNull Map<String, Object> map) {
        this.localMap.putAll(map);
    }

    private int size(final @NotNull Map<String, Object> map) {
        int size = map.size();
        for (Object o : map.values()) {
            if (o instanceof Map) {
                size += size((Map<String, Object>) o);
            }
        }
        return size;
    }

    // ----------------------------------------------------------------------------------------------------
    // Utility functions
    // ----------------------------------------------------------------------------------------------------

    public @NotNull Map<String, Object> toMap() {
        return this.localMap;
    }

    public @NotNull JSONObject toJsonObject() {
        return JsonUtils.getJsonFromMap(this.localMap);
    }

    // ----------------------------------------------------------------------------------------------------
    // Overridden methods form Object
    // ----------------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return this.localMap.hashCode();
    }

    @Override
    public String toString() {
        return this.localMap.toString();
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            val fileData = (FileData) obj;
            return this.localMap.equals(fileData.localMap);
        }
    }
}
