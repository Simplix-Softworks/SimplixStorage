package de.leonhard.storage.internal.provider;

import com.esotericsoftware.yamlbeans.YamlConfig;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils & Trove Used in {@link
 * de.leonhard.storage.internal.settings.DataType} Enum
 */
@UtilityClass
@Accessors(fluent = true, chain = true)
public class LightningProviders {

    @Setter
    private MapProvider mapProvider;
    @Setter
    private YamlConfig yamlConfig;
    @Setter
    private InputStreamProvider inputStreamProvider;
    @Setter
    private ExceptionHandler exceptionHandler;

    public MapProvider mapProvider() {
        if (mapProvider != null) {
            return mapProvider;
        }
        return mapProvider = new MapProvider() {
        };
    }

    public YamlConfig yamlConfig() {
        if (yamlConfig != null) {
            return yamlConfig;
        }

        val config = new YamlConfig();

        // Use unicode
        config.writeConfig.setEscapeUnicode(false);
        // Don't use anchors
        config.writeConfig.setAutoAnchor(false);
        // Never use write the classname above keys
        config.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
        return yamlConfig = config;
    }

    public InputStreamProvider inputStreamProvider() {
        if (inputStreamProvider != null) {
            return inputStreamProvider;
        }

        return inputStreamProvider = new InputStreamProvider() {
        };
    }

    public ExceptionHandler exceptionHandler() {
        if (exceptionHandler != null) {
            return exceptionHandler;
        }

        return exceptionHandler = new ExceptionHandler() {
        };
    }
}
