package de.leonhard.storage.util;

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
		//Use unicode
		config.writeConfig.setEscapeUnicode(false);
		//Don't use anchors
		config.writeConfig.setAutoAnchor(false);
		//Never use write the classname above keys
		config.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
		return yamlConfig = config;
	}
}
