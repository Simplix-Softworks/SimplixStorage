package de.leonhard.storage.internal.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@SuppressWarnings({"unchecked", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static JSONObject getJsonFromMap(Map<String, Object> map) throws JSONException {
		JSONObject jsonData = new JSONObject();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value instanceof Map<?, ?>) {
				value = getJsonFromMap((Map<String, Object>) value);
			}
			jsonData.put(key, value);
		}
		return jsonData;
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