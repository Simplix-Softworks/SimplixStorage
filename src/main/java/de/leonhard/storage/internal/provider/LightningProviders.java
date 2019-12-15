package de.leonhard.storage.internal.provider;

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

	public MapProvider getMapProvider() {
		if (mapProvider != null) {
			return mapProvider;
		}

		return mapProvider = new MapProvider() {
		};
	}
}
