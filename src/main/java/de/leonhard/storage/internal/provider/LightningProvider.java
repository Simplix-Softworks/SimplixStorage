package de.leonhard.storage.internal.provider;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
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
	@Getter
	@Setter
	@NotNull
	private Map<String, Object> defaultMapImplementation = new HashMap<>();
	@Getter
	@Setter
	@NotNull
	private Map<String, Object> sortedMapImplementation = new LinkedHashMap<>();
}
