package de.leonhard.storage.internal.base.interfaces;

import java.util.Map;
import java.util.Set;
import org.json.JSONObject;


public interface Data {

	boolean containsKey(final String key);

	Object get(final String key);

	void insert(final String key, final Object value);

	void remove(final String key);

	Set<String> keySet();

	Set<String> keySet(final String key);

	Set<String> singleLayerKeySet();

	Set<String> singleLayerKeySet(final String key);

	int singleLayerSize();

	int singleLayerSize(final String key);

	int size();

	int size(final String key);

	JSONObject toJsonObject();

	Map<String, Object> toMap();
}