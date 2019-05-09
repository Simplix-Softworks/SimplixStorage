package de.leonhard.storage;

import de.leonhard.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Json extends StorageCreator implements StorageBase {
    private final String path, name;
    private JSONObject object;
    private File file;


    /**
     * Creates a .json file where you can put your data in.+
     *
     * @param name Name of the .json file
     * @param path Absolute path, where the .json file should be created.
     */

    public Json(final String name, final String path) {
        file = new File(path + File.separator + name + ".json");

        this.name = name;
        this.path = path;
        if (!file.exists()) {
            try {
                create(path, name, FileType.JSON);
                object = new JSONObject();
                Writer writer = new PrintWriter(new FileWriter(path + File.separator + name + ".json"));
                writer.write(object.toString(2));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        JSONTokener tokener = new JSONTokener(fis);
        object = new JSONObject(tokener);
    }

    //TODO IMMER auf TODOS überprüfen.
    //TODO In plugin Beschreibung kommentieren -> Verhältnis zu Bukkit-Methode!

    @Override
    public void setDefault(String key, Object value) {
        if (contains(key)) {
            return;
        }
        set(key, value);
    }


    public synchronized void reload() {


        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        JSONTokener tokener = new JSONTokener(fis);
        object = new JSONObject(tokener);

    }


    private <T> T getNestedObject(String key) throws JSONException, NullPointerException {
        String[] parts = key.split("\\.");
        Map<?, ?> map = getMap(parts[0]);

        return (T) map.get(parts[1]);
    }


    @Override
    public String getString(final String key) {
        reload();

        if (!contains(key))
            return null;


        if (key.contains(".")) {
            return (String) getNestedObject(key);
        }


        return object.getString(key);

    }


    @Override
    public long getLong(final String key) {
        reload();
        if (!contains(key))
            return 0;
        if (key.contains(".")) {
            return (Long) getNestedObject(key);
        }
        return object.getLong(key);

    }

    @Override
    public int getInt(final String key) {
        reload();

        if (!contains(key))
            return 0;

        if (key.contains(".")) {
            return (int) getNestedObject(key);
        }
        return object.getInt(key);


    }

    @Override
    public byte getByte(final String key) {
        reload();

        if (!contains(key))
            return 0;

        if (key.contains(".")) {
            return getNestedObject(key);
        }

        return (byte) object.get(key);

    }

    @Override
    public boolean getBoolean(final String key) {
        reload();

        if (!contains(key))
            return false;

        if (key.contains(".")) {
            return (boolean) getNestedObject(key);
        }
        return object.getBoolean(key);//Support for Strings?

    }

    @Override
    public float getFloat(final String key) {
        reload();

        if (!contains(key))
            return 0;

        if (key.contains(".")) {
            if (getNestedObject(key) instanceof Double) {
                return (float) (double) getNestedObject(key);
            }
            return (getNestedObject(key) instanceof Integer) ? (float) (int) getNestedObject(key) : getNestedObject(key);//TrobleShooting: Integer not castable to Double
            // -> Wrapper class
        }
        return (float) object.get(key);

    }

    @Override
    public double getDouble(final String key) {
        reload();

        if (!contains(key))
            return 0;

        if (key.contains(".")) {
            return (getNestedObject(key) instanceof Integer) ? (double) (int) getNestedObject(key) : getNestedObject(key);//TrobleShooting: Integer not castable to Double
            // -> Wrapper class
        }
        return object.getDouble(key);

    }

    @Override
    public List<?> getList(final String key) {
        reload();
        if (!contains(key))
            return new ArrayList<>();

        if (key.contains(".")) {
            return getNestedObject(key);
        }

        Object object = this.object.get(key);
        JSONArray ja = new JSONArray(object.toString());
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            list.add(ja.get(i));
        }
        return (list == null) ? new ArrayList<>() : list; //TODO CHECK!

    }

    @Override
    public List<String> getStringList(final String key) { //TODO Wenn nur ein Element in der Liste ist, keine ClassCast Exception!
        reload();

        if (!contains(key))
            return new ArrayList<>();

        if (key.contains(".")) {
            return getNestedObject(key);
        }

        List<?> temp = getList(key);
        List<String> list = new ArrayList<>();
        for (Object o : temp) {
            if (o instanceof String) {
                list.add((String) o);
            }
        }
        return (list == null) ? new ArrayList<String>() : list;

    }

    @Override
    public List<Integer> getIntegerList(final String key) {
        reload();
        if (!contains(key))
            return new ArrayList<>();
        if (key.contains(".")) {
            return getNestedObject(key);
        }

        List<?> temp = getList(key);
        List<Integer> list = new ArrayList<>();
        for (Object o : temp) {
            if (o instanceof Integer) {
                list.add((Integer) o);
            } else if (o instanceof String) {
                list.add(Integer.valueOf((String) o));
            }
        }
        return (list == null) ? new ArrayList<>() : list;
    }

    @Override
    public List<Byte> getByteList(final String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();


        if (key.contains(".")) {
            return getNestedObject(key);
        }

        List<?> temp = getList(key);
        List<Byte> list = new ArrayList<>();
        for (Object o : temp) {
            if (o instanceof Byte) {
                list.add((Byte) o);
            } else if (o instanceof String) {
                list.add(Byte.valueOf((String) o));
            }
        }
        return (list == null) ? new ArrayList<>() : list;
    }

    @Override
    public List<Long> getLongList(final String key) {
        reload();

        if (!contains(key))
            return new ArrayList<>();

        if (key.contains(".")) {
            return getNestedObject(key);
        }

        List<?> temp = getList(key);
        List<Long> list = new ArrayList<>();
        for (Object o : temp) {
            if (o instanceof Long) {
                list.add((Long) o);
            } else if (o instanceof String) {
                list.add(Long.valueOf((String) o));
            }
        }
        return (list == null) ? new ArrayList<Long>() : list;

    }

    @Override
    public Map getMap(final String key) {//TODO Check
        reload();

        if (!contains(key))
            return new HashMap();

        Object map;
        try {
            map = object.get(key);
        } catch (JSONException e) {
            return new HashMap<>();
        }
        if (map instanceof Map) {
            return (Map<?, ?>) object.get(key);
        } else if (map instanceof JSONObject) {
            Map<String, Object> hash = JsonUtil.jsonToMap((JSONObject) map);
            return hash;
        }
        throw new IllegalArgumentException("Json does not contain: '" + key + "'.");
    }


    @Override
    public void set(final String key, final Object value) {
        synchronized (this) {
            if (key.contains(".")) {
                String[] parts = key.split("\\.");
                HashMap<String, Object> map = (HashMap<String, Object>) getMap(parts[0]);
                if (parts.length == 2) {
                    map.put(parts[1], value);
                }
                object.put(parts[0], map);
                try {
                    Writer writer = new PrintWriter(new FileWriter(path + File.separator + name + ".json"));
                    writer.write(object.toString(2));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            object.put(key, value);
            try {
                Writer writer = new PrintWriter(new FileWriter(path + File.separator + name + ".json"));
                writer.write(object.toString(2));
                writer.close();
            } catch (IOException e) {
                System.err.println("Couldn' t set " + key + " " + value + e.getMessage());
            }
        }
    }

    @Override
    public boolean contains(String key) {//TODO nestedObject compatibility!
        reload();
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            return object.has(parts[0]) && getMap(parts[0]).containsKey(parts[1]);
        }

        return object.has(key);
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getFilePath() {
        return file.getAbsolutePath();
    }
}
