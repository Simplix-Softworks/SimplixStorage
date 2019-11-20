package de.leonhard.storage.utils;

import com.esotericsoftware.yamlbeans.YamlConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class YamlUtils {
	@Setter
	private static YamlConfig yamlConfig;

	public static YamlConfig getDefaultYamlConfig() {
		if (yamlConfig != null) {
			return yamlConfig;
		}
		YamlConfig config = new YamlConfig();
		config.writeConfig.setEscapeUnicode(false);
		config.writeConfig.setAutoAnchor(false);
		return yamlConfig = config;
	}
}
