package de.leonhard.storage.internal.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@SuppressWarnings("WeakerAccess")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        keysItr.forEachRemaining(key -> map.put(key, getValue(object.get(key))));
        return map;
    }

    private static Object getValue(Object o) {
        if (o instanceof JSONArray) {
            return toList((JSONArray) o);
        } else if (o instanceof JSONObject) {
            return toMap((JSONObject) o);
        } else {
            return o;
        }

    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getValue(array.get(i)));
        }
        return list;
    }
}