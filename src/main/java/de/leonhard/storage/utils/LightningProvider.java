package de.leonhard.storage.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils & Trove
 * Used in {@link de.leonhard.storage.internal.settings.DataType} Enum
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LightningProvider {

	private static final Map<Class<?>, Object> CLASS_OBJECT_MAP = Collections.synchronizedMap(new HashMap<>());

	public static boolean registerProvider(final Class<?> key, final Object value) {
		if (CLASS_OBJECT_MAP.containsKey(key)) {
			return false;
		}

		CLASS_OBJECT_MAP.put(key, value);
		return true;
	}

	public static Map getDefaultMapImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(Map.class)) {
			return new HashMap<>();
		}
		return (Map) CLASS_OBJECT_MAP.get(Map.class);
	}

	public static Map getSortedMapImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(SortedMap.class)) {
			return new LinkedHashMap<>();
		}
		return (Map) CLASS_OBJECT_MAP.get(Map.class);
	}

	public static List<?> getDefaultListImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(List.class)) {
			return new ArrayList<>();
		}
		return (List) CLASS_OBJECT_MAP.get(List.class);
	}

	public static List getSortedListImplementation() {
		if (!CLASS_OBJECT_MAP.containsKey(LinkedList.class)) {
			return new ArrayList<>();
		}
		return (List) CLASS_OBJECT_MAP.get(LinkedList.class);
	}
}
