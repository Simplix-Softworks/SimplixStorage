package de.leonhard.storage.util;

import java.util.HashMap;
import java.util.Map;

public class FileData {

    private HashMap<String, Object> localMap;

    public FileData(Map<String, Object> map) {
        localMap = new HashMap<>(map);
    }


    public Object getObjectFromMap(final String key) {
        final String[] parts = key.split("\\.");
        return getObjectFromMap(localMap, parts, 0);
    }

    @SuppressWarnings("unchecked")
    private Object getObjectFromMap(final Map<String, Object> map, final String[] key, final int id) {
        if (id < key.length - 1) {
            if (map.get(key[id]) instanceof Map) {
                Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
                return getObjectFromMap(tempMap, key, id + 1);
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


    public boolean contains(String key) {
        String[] parts = key.split("\\.");
        return contains(localMap, parts, 0);
    }

    @SuppressWarnings("unchecked")
    private boolean contains(final Map<String, Object> map, String[] key, int id) {
        if (id < key.length - 1) {
            if (map.containsKey(key[id]) && map.get(key[id]) instanceof Map) {
                Map<String, Object> tempMap = (Map<String, Object>) map.get(key[id]);
                return contains(tempMap, key, id + 1);
            } else {
                return false;
            }
        } else {
            return map.containsKey(key[id]);
        }
    }


    public void remove(final String key) {
        if (contains(key)) {
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

    public Map<String, Object> toMap() {
        if (localMap == null) {
            throw new IllegalStateException("Couldn't get null");
        } else {
            return localMap;
        }
    }
}