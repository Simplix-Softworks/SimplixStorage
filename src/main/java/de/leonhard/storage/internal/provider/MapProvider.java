package de.leonhard.storage.internal.provider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class MapProvider {

    public Map<String, Object> getMapImplementation() {
        return new HashMap<>();
    }

    public Map<String, Object> getSortedMapImplementation() {
        return new LinkedHashMap<>();
    }
}
