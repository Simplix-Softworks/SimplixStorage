package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.internal.provider.MapProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * An Enum defining how the Data should be stored
 */
@RequiredArgsConstructor
public enum DataType {

    // Sorted data type
    SORTED {
        @Override
        public @NotNull Map<String, Object> getMapImplementation() {
            return mapProvider.getSortedMapImplementation();
        }
    },

    // Unsorted data type
    UNSORTED {
        @Override
        public @NotNull Map<String, Object> getMapImplementation() {
            return mapProvider.getSortedMapImplementation();
        }
    };

    private static final MapProvider mapProvider = LightningProviders.mapProvider();

    public static @NotNull DataType forConfigSetting(final ConfigSettings configSettings) {
        // Only Configs needs the preservation of the order of the keys
        if (ConfigSettings.PRESERVE_COMMENTS.equals(configSettings)) {
            return SORTED;
        }
        // In all other cases using the normal HashMap is better to save memory.
        return UNSORTED;
    }

    public @NotNull Map<String, Object> getMapImplementation() {
        throw new AbstractMethodError("Not implemented");
    }
}
