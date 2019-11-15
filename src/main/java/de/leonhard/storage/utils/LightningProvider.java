package de.leonhard.storage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LightningProvider {
	private final Map<Class<?>, Object> CLASS_OBJECT_MAP = Collections.synchronizedMap(new HashMap<>());


	public boolean registerProvider(final Class<?> key, final Object value) {
		if (CLASS_OBJECT_MAP.containsKey(key)) {
			return false;
		}

		CLASS_OBJECT_MAP.put(key, value);
		return true;
	}

	public Map<String, Object> getDefaultMapImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(Map.class)) {
			return new HashMap<>();
		}
		return (Map<String, Object>) CLASS_OBJECT_MAP.get(Map.class);
	}

	public List<?> getDefaultListImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(List.class)) {
			return new ArrayList<>();
		}
		return (List<?>) CLASS_OBJECT_MAP.get(List.class);
	}
}
