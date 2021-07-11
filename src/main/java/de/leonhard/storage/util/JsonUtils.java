package de.leonhard.storage.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
@UtilityClass
public class JsonUtils {

    public @NotNull Map<String, Object> jsonToMap(final @NotNull JSONObject jsonObject) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if (jsonObject != JSONObject.NULL) {
            retMap = toMap(jsonObject);
        }

        return retMap;
    }

    public @NotNull JSONObject getJsonFromMap(final @NotNull Map<String, Object> map) throws JSONException {
        val jsonData = new JSONObject();

        for (val entry : map.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value instanceof Map) {
                value = getJsonFromMap((Map<String, Object>) value);
            }

            jsonData.put(key, value);
        }

        return jsonData;
    }

    public @NotNull Map<String, Object> toMap(final @NotNull JSONObject jsonObject) throws JSONException {
        final Map<String, Object> map = new HashMap<>();

        val keysItr = jsonObject.keys();
        keysItr.forEachRemaining(key -> map.put(key, getValue(jsonObject.get(key))));
        return map;
    }

    public @NotNull List<Object> toList(final @NotNull JSONArray array) throws JSONException {
        final List<Object> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(getValue(array.get(i)));
        }

        return list;
    }

    private Object getValue(final Object obj) {
        if (obj instanceof JSONArray) {
            return toList((JSONArray) obj);
        } else if (obj instanceof JSONObject) {
            return toMap((JSONObject) obj);
        } else {
            return obj;
        }
    }
}
