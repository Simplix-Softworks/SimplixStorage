package de.leonhard.storage.utils;

import com.esotericsoftware.yamlbeans.YamlConfig;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class YamlUtils {
	@Setter
	private YamlConfig yamlConfig;

	public YamlConfig getDefaultYamlConfig() {
		if (yamlConfig != null) {
			return yamlConfig;
		}
		YamlConfig config = new YamlConfig();
		config.writeConfig.setEscapeUnicode(false);
		config.writeConfig.setAutoAnchor(false);
		return yamlConfig = config;
	}
}
