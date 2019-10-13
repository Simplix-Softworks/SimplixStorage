package de.leonhard.storage.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileData {

    private HashMap<String, Object> localMap;

    public FileData(Map<String, Object> map) {
        localMap = new HashMap<>(map);
    }


    public Object get(final String key) {
        final String[] parts = key.split("\\.");
        return get(localMap, parts, 0);
    }

    @SuppressWarnings("unchecked")
    private Object get(final Map<String, Object> map, final String[] key, final int id) {
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


    public void insert(final String key, final Object value) {
        final String[] parts = key.split("\\.");
        localMap.put(parts[0], insert(localMap, parts, value, 1));
    }

    @SuppressWarnings("unchecked")
    private Object insert(final Map<String, Object> map, final String[] key, final Object value, final int id) {
        if (id < key.length) {
            Map<String, Object> tempMap = new HashMap<>(map);
            Map<String, Object> childMap = map.containsKey(key[id]) ? (map.get(key[id]) instanceof Map ? (Map<String, Object>) map.get(key[id]) : new HashMap<>()) : new HashMap<>();
            tempMap.put(key[id], insert(childMap, key, value, id + 1));
            return tempMap;
        } else {
            return value;
        }
    }


    public boolean containsKey(String key) {
        String[] parts = key.split("\\.");
        return containsKey(localMap, parts, 0);
    }

    @SuppressWarnings("unchecked")
    private boolean containsKey(final Map<String, Object> map, String[] key, int id) {
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


    public void remove(final String key) {
        if (containsKey(key)) {
            final String[] parts = key.split("\\.");
            remove(localMap, parts, 0);
        }
    }

    private void remove(final Map<String, Object> map, final String[] key, final int id) {
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

    public Set<String> keySet() {
        return localMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public Set<String> keySet(String key) {
        if (get(key) instanceof Map) {
            return ((Map<String, Object>) get(key)).keySet();
        } else {
            return new HashSet<>();
        }
    }


    public Set<String> deepKeySet() {
        return deepKeySet(localMap);
    }

    @SuppressWarnings("unchecked")
    public Set<String> deepKeySet(String key) {
        if (get(key) instanceof Map) {
            Map tempMap = (Map<String, Object>) get(key);
            return deepKeySet(tempMap);
        } else {
            return new HashSet<>();
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> deepKeySet(final Map<String, Object> map) {
        Set<String> localSet = new HashSet<>();
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                for (String tempKey : deepKeySet((Map<String, Object>) map.get(key))) {
                    localSet.add(key + "." + tempKey);
                }
            }
        }
        return localSet;
    }

    public Map<String, Object> toMap() {
        if (localMap == null) {
            throw new IllegalStateException("Couldn't get null");
        } else {
            return localMap;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            FileData fileData = (FileData) obj;
            return this.localMap.equals(fileData.localMap);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.localMap.hashCode();
    }

    @Override
    public String toString() {
        return this.localMap.toString();
    }
}