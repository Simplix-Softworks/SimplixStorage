package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.internal.provider.MapProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * An Enum defining how the Data should be stored
 */
@RequiredArgsConstructor
public enum DataType {
  SORTED {
    @Override
    public Map<String, Object> getMapImplementation() {
      return mapProvider.getSortedMapImplementation();
    }
  },

  UNSORTED {
    @Override
    public Map<String, Object> getMapImplementation() {
      return mapProvider.getSortedMapImplementation();
    }
  };

  private static final MapProvider mapProvider = LightningProviders.mapProvider();

  public static DataType forConfigSetting(final ConfigSettings configSettings) {
    // Only Configs needs the preservation of the order of the keys
    if (ConfigSettings.PRESERVE_COMMENTS.equals(configSettings)) {
      return SORTED;
    }
    // In all other cases using the normal HashMap is better to save memory.
    return UNSORTED;
  }

  public Map<String, Object> getMapImplementation() {
    throw new AbstractMethodError("Not implemented");
  }
}
