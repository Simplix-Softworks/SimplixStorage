package de.leonhard.storage.internal.provider;

import com.esotericsoftware.yamlbeans.YamlConfig;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils & Trove
 * Used in {@link de.leonhard.storage.internal.settings.DataType} Enum
 */
@UtilityClass
public class LightningProviders {
	@Setter
	private MapProvider mapProvider;
	@Setter
	private YamlConfig yamlConfig;
	@Setter
	private InputStreamProvider inputStreamProvider;

	public MapProvider getMapProvider() {
		if (mapProvider != null) {
			return mapProvider;
		}

		return mapProvider = new MapProvider() {
		};
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

	public InputStreamProvider getInputStreamProvider() {
		if (inputStreamProvider != null) {
			return inputStreamProvider;
		}

		return inputStreamProvider = new InputStreamProvider() {
		};
	}
}
