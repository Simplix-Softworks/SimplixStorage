package de.leonhard.storage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@SuppressWarnings({"unchecked", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	public static Map<String, Object> jsonToMap(JSONObject jsonObject) throws JSONException {
		Map<String, Object> retMap = new HashMap<>();

		if (jsonObject != JSONObject.NULL) {
			retMap = toMap(jsonObject);
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

	public static Map<String, Object> toMap(JSONObject jsonObject) throws JSONException {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = jsonObject.keys();
		keysItr.forEachRemaining(key -> map.put(key, getValue(jsonObject.get(key))));
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getValue(array.get(i)));
		}
		return list;
	}

	private static Object getValue(Object obj) {
		if (obj instanceof JSONArray) {
			return toList((JSONArray) obj);
		} else if (obj instanceof JSONObject) {
			return toMap((JSONObject) obj);
		} else {
			return obj;
		}
	}
}