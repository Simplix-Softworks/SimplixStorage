package de.leonhard.storage;

import de.leonhard.storage.util.HashMapUtil;

import java.util.*;

public class YamlObject {
    private Object object;
    private HashMap<String, Object> hashMap;


    YamlObject(Object object) {
        this.object = object;
        this.hashMap = toHashMap();
    }


    //SINGLETONs?

    HashMap toHashMap() {
        return (object == null) ? new HashMap<>() : (HashMap) object;
    }


    Object get(String key) {
        if (key.contains(".")) {
            return HashMapUtil.contains(key, hashMap) ? HashMapUtil.get(key, hashMap) : null;
        }
        return hashMap.containsKey(key) ? toHashMap().get(key) : null;
    }

    boolean getBoolean(final String key) {
        if (get(key) instanceof String) {
            return (((String) get(key)).equalsIgnoreCase("true"));
        }
        return (boolean) get(key);
    }

    byte getByte(final String key) {
        if (get(key) instanceof String)
            return Byte.parseByte(get(key).toString());
        return (byte) get(key);
    }

    int getInt(final String key) {
        if (get(key) instanceof String)
            return Integer.parseInt(get(key).toString());
        return (int) get(key);
    }

    float getFloat(final String key) {
        if (get(key) instanceof String) {
            return Float.parseFloat(get(key).toString());
        }
        return (float) get(key);
    }

    double getDouble(final String key) {
        if (get(key) instanceof String)
            return Double.parseDouble(get(key).toString());
        return (double) get(key);
    }

    long getLong(final String key) {
        if (get(key) instanceof String)
            return Long.parseLong(get(key).toString());
        return (long) get(key);
    }


    String getString(final String key) {
        return (String) get(key);
    }

    public List<?> getList(String key) {
        return (List) get(key);
    }

    /**
     * Get String List
     *
     * @param key Path to String List in YAML-FIle
     * @return List
     */
    List<String> getStringList(String key) {
        return (List<String>) get(key);
    }

    List<Integer> getIntegerList(String key) {
        return (List<Integer>) get(key);
    }

    List<Byte> getByteList(String key) {
        return (List<Byte>) get(key);
    }

    List<Long> getLongList(String key) {
        return (List<Long>) get(key);
    }

    private Map getMap(final String key) {
        return (Map) toHashMap().get(key);
    }


    void put(String key, Object value) {
        if (key.contains(".")) {
            hashMap = (HashMap) HashMapUtil.stringToMap(key, value, hashMap);
            object = hashMap;
            return;
            //
        }

        hashMap.put(key, value);
        object = hashMap;
    }


    @Override
    public String toString() {
        return object.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return object.toString().equals(obj.toString());
    }


    public boolean contains(String key) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            return toHashMap().containsKey(parts[0]) && getMap(parts[0]).containsKey(parts[1]);
        }

        return toHashMap().containsKey(key);
    }


}