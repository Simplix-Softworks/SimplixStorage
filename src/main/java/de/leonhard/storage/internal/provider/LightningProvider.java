package de.leonhard.storage.internal.provider;

import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface for registering more powerful Map/List implementation than the default JDK one's
 * examples for these implementations are FastUtils & Trove
 * Used in {@link de.leonhard.storage.internal.settings.DataType} Enum
 */
@UtilityClass
public class LightningProvider {
	@Setter
	private Map<String, Object> defaultMapImplementation;
	@Setter
	private Map<String, Object> sortedMapImplementation;

	public Map<String, Object> getDefaultMapImplementation() {
		if (defaultMapImplementation != null) {
			//Cloning our values
			return new HashMap<>(defaultMapImplementation);
		}

		return defaultMapImplementation = new HashMap<>();

	}

	public Map<String, Object> getSortedMapImplementation() {
		if (defaultMapImplementation != null) {
			//Cloning our values
			return new LinkedHashMap<>(sortedMapImplementation);
		}

		return defaultMapImplementation = new LinkedHashMap<>();
	}
}
