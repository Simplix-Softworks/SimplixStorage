package de.leonhard.storage.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HashMapUtil {


    private HashMapUtil() {

    }

    public static Map stringToMap(final String string, final Object value, final Map object) {
        if (string.contains(".")) {
            String[] parts = string.split("\\.");

            HashMap keyMap = new HashMap();

            int j = 0;
            for (int i = parts.length - 1; i > 0; i--) {

                final String key = getFirst(string, j);

                if (contains(key, object)) {
                    final Object obj = get(key, object);
                    if (obj instanceof Map) {
                        if (i == parts.length - 2) {
//                            System.out.println(i + " key: " + " part " + parts[i+1] + " '" + key + " KEYMAP PRE " + keyMap);
                            keyMap = (HashMap) deepMerge((Map) keyMap.clone(), (Map) obj);
                            if (keyMap.containsKey(parts[i + 1])){
                                keyMap.remove(parts[i+1]);
                                keyMap.put(parts[i+1], value); //PUTTING THE VALUE
                            }

//                            System.out.println(i + " key: " + key + " KEYMAP AFTER " + keyMap);

                        } else {
//                            System.out.println(i + " key: " + key + " KEYMAP PRE " + keyMap);
                            keyMap = (HashMap) deepMerge((Map) keyMap.clone(), (Map) obj);
//                            System.out.println(i + " key: " + key + " KEYMAP AFTER " + keyMap);
                        }
                    } else {
//                        System.out.println("PRE" + keyMap);
                        keyMap.put(parts[i], obj);//NOW BUGFREE
//                        System.out.println("AFTER" + keyMap);
                    }
                }
                if (i == parts.length - 1) {
                    keyMap.put(parts[parts.length - 1], value); //ADDED DIE VALUE -> BUGGFREI
                } else {
//                    System.out.println(i + " " + (parts.length - 1) + " " + "SECOND " + keyMap + " key " + key);

                    if (keyMap.containsKey(parts[i])) {
                        keyMap.remove(parts[i]);
                        keyMap.put(parts[i + 1], value);
                    }


                    HashMap preResult = new HashMap();
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
        return new HashMap();
    }

    public static boolean contains(String string, final Map object) {
        if (string.contains(".")) {
            boolean result = true;
            String[] parts = string.split("\\.");
            Map preResult = object;
            for (int i = 0; i < parts.length; i++) {
                if (!preResult.containsKey(parts[i])) {
//                    System.out.println("HASHMAP CONTAINT NICHT! " + preResult + " PARTS: " + Arrays.toString(parts) + " PART " + parts[i]);
                    return false;
                }
                if (!(preResult.get(parts[i]) instanceof HashMap) && i != parts.length - 1) {
//                    System.out.println("KEINE HASHMAP " + preResult + "  PARTS: " + Arrays.toString(parts) + " PART " + parts[i]);
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

    private static String getFirst(final String string, int offset) {
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

    private static Map deepMerge(Map original, Map newMap) {
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
