package de.leonhard.storage;

import de.leonhard.storage.util.HashMapUtil;

import java.util.*;

public class YamlObject {
    private Object object;
    private HashMap<String, Object> hashMap;


    public YamlObject(Object object) {
        this.object = object;
        this.hashMap = toHashMap();
    }


    //SINGLETONs?

    public HashMap toHashMap() {
        return (object == null) ? new HashMap<>() : (HashMap) object;
    }


    public Object get(String key) {
        if (key.contains(".")) {
            return HashMapUtil.contains(key, hashMap) ? HashMapUtil.get(key, hashMap) : null;
        }
        return hashMap.containsKey(key) ? toHashMap().get(key) : null;
    }

    public boolean getBoolean(final String key) {
        System.out.println(get(key));
        if (get(key) instanceof String) {
            return (((String) get(key)).equalsIgnoreCase("true"));
        }
        return (boolean) get(key);
    }

    public byte getByte(final String key) {
        if (get(key) instanceof String)
            return Byte.parseByte(get(key).toString());
        return (byte) get(key);
    }

    public int getInt(final String key) {
        if (get(key) instanceof String)
            return Integer.parseInt(get(key).toString());
        return (int) get(key);
    }

    public float getFloat(final String key) {
        if (get(key) instanceof String) {
            return Float.parseFloat(get(key).toString());
        }
        return (float) get(key);
    }

    public double getDouble(final String key) {
        if (get(key) instanceof String)
            return Double.parseDouble(get(key).toString());
        return (double) get(key);
    }

    public long getLong(final String key) {
        if (get(key) instanceof String)
            return Long.parseLong(get(key).toString());
        return (long) get(key);
    }


    public String getString(final String key) {
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
    public List<String> getStringList(String key) {
        return (List<String>) get(key);
    }

    public List<Integer> getIntegerList(String key) {
        return (List<Integer>) get(key);
    }

    public List<Byte> getByteList(String key) {
        return (List<Byte>) get(key);
    }

    public List<Long> getLongList(String key) {
        return (List<Long>) get(key);
    }

    private Map getMap(final String key) {
        return (Map) toHashMap().get(key);
    }


    public void put(String key, Object value) {
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