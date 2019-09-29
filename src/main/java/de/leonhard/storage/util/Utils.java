package de.leonhard.storage.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map stringToMap(final String string) {
        final Map result = new HashMap();
        final String str = string.replace("{", "").replace("}", "");
        final String[] array = str.split(",");
        for (String s : array) {
            final String[] splitted = s.split("=");
            if (splitted.length != 2)
                continue;
            result.put(splitted[0].replace(" ", ""), splitted[1]);
        }
        return result;
    }

    //Method to create nested objects from String keys
    public static Map<String, Object> stringToMap(final String string, final Object value, final Map object) {
        if (!string.contains(".")) {
            object.put(string, value);
            return object;
        }
        final String[] parts = string.split("\\.");
        HashMap<String, Object> keyMap = new HashMap<>();


        int j = 0;
        for (int i = parts.length - 1; i > 0; i--) {

            final String key = getFirst(string, j);

            if (contains(key, object)) {
                final Object obj = get(key, object);
                if (obj instanceof Map) {
                    if (i == parts.length - 2) {
                        keyMap = (HashMap<String, Object>) deepMerge((Map) keyMap.clone(), (Map) obj);
                        if (keyMap.containsKey(parts[i + 1])) {
                            keyMap.remove(parts[i + 1]);
                            keyMap.put(parts[i + 1], value); //PUTTING THE VALUE
                        }
                    } else {
                        keyMap = (HashMap<String, Object>) deepMerge((Map) keyMap.clone(), (Map) obj);
                    }
                } else {
                    keyMap.put(parts[i], obj);//NOW BUGFREE
                }
            }
            if (i == parts.length - 1) {
                keyMap.put(parts[parts.length - 1], value); //ADDED DIE VALUE -> BUGGFREI
            } else {

                if (keyMap.containsKey(parts[i])) {
                    keyMap.remove(parts[i]);
                    keyMap.put(parts[i + 1], value);
                }


                HashMap<String, Object> preResult = new HashMap<>();
                preResult.put(parts[i], keyMap);
                keyMap = preResult;
            }
            j++;
        }
        //Merging
        final Map result = new HashMap();


        result.put(parts[0], keyMap);


        return deepMerge(object, result);
    }


    public static boolean contains(String string, final Map object) {
        if (string.contains(".")) {
            boolean result = true;
            String[] parts = string.split("\\.");
            Map preResult = object;
            for (int i = 0; i < parts.length; i++) {
                if (!preResult.containsKey(parts[i])) {
                    return false;
                }
                if (!(preResult.get(parts[i]) instanceof HashMap) && i != parts.length - 1) {
                    result = false;
                    return false;
                }

                if (preResult.containsKey(parts[i]) && preResult.get(parts[i]) instanceof HashMap) {
                    preResult = (HashMap) preResult.get(parts[i]);

                } else return preResult.containsKey(parts[i]) && i == parts.length - 1;

            }
            return true;
        }
        return object.containsKey(string);
    }

    public static Object get(final String key, final Map object) {
        if (key.contains(".")) {
            String[] parts = key.split("\\.");
            Map preResult = (get(parts[0], object) == null || (!(get(parts[0], object) instanceof Map)) ? new HashMap() : (HashMap) get(parts[0], object));//WARNING RECURSION!
            for (int i = 1; i < parts.length; i++) {
                if (!(preResult.get(parts[i]) instanceof HashMap) || i == parts.length - 1)
                    return preResult.get(parts[i]);
                preResult = (HashMap) preResult.get(parts[i]);
            }
        }
        return object.containsKey(key) ? object.get(key) : null;
    }

    public static Map<String, Object> remove(final Map<String, Object> map, final String key) {
        final String[] parts = key.split("\\.");
        final String withoutLast = getFirst(key, 1);
        final String first = parts[0];
        final String last = parts[parts.length - 1];


        final Map tmp = (Map) get(withoutLast, map); //Getter

        if (tmp == null)
            throw new IllegalStateException("Map mustn't be null");

        tmp.remove(last);

        Map<String, Object> preResult = stringToMap(withoutLast, tmp, map);

        return deepMerge(map, preResult);
    }

    public static String getFirst(final String string, int offset) {
        final ArrayList<String> strings = new ArrayList<>(Arrays.asList(string.split("\\.")));
        final StringBuilder sb = new StringBuilder();

        final int max = strings.size() - offset;
        int i = 0;
        for (final String str : strings) {
            if (i < max) {
                i++;
                if (i != 1) {
                    sb.append(".");
                }
                sb.append(str);
            }
        }


        return sb.toString();
    }

    public static Map<String, String> convertStringToMap(String str) {
        String[] tokens = str.split("[ =]");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < tokens.length - 1; ) map.put(tokens[i++], tokens[i++]);
        return map;
    }

    //Method to merge Maps
    private static Map<String, Object> deepMerge(Map original, Map newMap) {
        for (Object key : newMap.keySet()) {
            if (newMap.get(key) instanceof Map && original.get(key) instanceof Map) {
                Map originalChild = (Map) original.get(key);
                Map newChild = (Map) newMap.get(key);
                original.put(key, deepMerge(originalChild, newChild));
            } else {
                original.put(key, newMap.get(key));
            }
        }
        return original;
    }
}
