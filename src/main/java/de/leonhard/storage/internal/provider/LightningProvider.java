package de.leonhard.storage.internal.provider;

import com.esotericsoftware.yamlbeans.YamlConfig;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils & Trove
 * Used in {@link de.leonhard.storage.internal.settings.DataType} Enum
 * <p>
 * All getters are lazy.
 */
@UtilityClass
//Not yet in use
public class LightningProvider {
    @Setter
    private Map<String, Object> defaultMapImplementation;
    @Setter
    private Map<String, Object> sortedMapImplementation;
    @Setter
    private YamlConfig yamlConfig;

    //Coming soon.
    public Map<String, Object> getDefaultMapImplementation() {
        if (defaultMapImplementation != null) {
            return defaultMapImplementation;
        }

        return defaultMapImplementation = new HashMap<>();
    }

    public Map<String, Object> getSortedMapImplementation() {
        if (defaultMapImplementation != null) {
            return sortedMapImplementation;
        }

        return defaultMapImplementation = new LinkedHashMap<>();
    }

    public YamlConfig getYamlConfig() {
        if (yamlConfig != null) {
            return yamlConfig;
        }
        YamlConfig config = new YamlConfig();
        //Use unicode
        config.writeConfig.setEscapeUnicode(false);
        //Don't use anchors
        config.writeConfig.setAutoAnchor(false);
        //Never use write the classname above keys
        config.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
        return yamlConfig = config;
    }
}
