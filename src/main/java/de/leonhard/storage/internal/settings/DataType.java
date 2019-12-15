package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.provider.LightningProviders;

import java.util.Map;

/**
 * An Enum defining how the Data should be stored
 */
public enum DataType {

	SORTED {
		@Override
		public Map<String, Object> getMapImplementation() {
			return LightningProviders.getMapProvider().getSortedMapImplementation();
		}
	},

	UNSORTED {
		@Override
		public Map<String, Object> getMapImplementation() {
			return LightningProviders.getMapProvider().getSortedMapImplementation();
		}
	};

	public static DataType fromConfigSettings(ConfigSettings configSettings) {
		//Only Configs needs the preservation of the order of the keys
		if (ConfigSettings.PRESERVE_COMMENTS.equals(configSettings)) {
			return SORTED;
		}
		//In all other cases using the normal HashMap is better to save memory.
		return UNSORTED;
	}

	public Map<String, Object> getMapImplementation() {
		throw new AbstractMethodError("Not implemented");
	}
}
