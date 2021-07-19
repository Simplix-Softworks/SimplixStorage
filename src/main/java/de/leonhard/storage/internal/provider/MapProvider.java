package de.leonhard.storage.internal.provider;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class MapProvider {

  @SuppressWarnings("unused")
  public @NotNull Map<String, Object> getMapImplementation() {
    return new HashMap<>();
  }

  public @NotNull Map<String, Object> getSortedMapImplementation() {
    return new LinkedHashMap<>();
  }
}
