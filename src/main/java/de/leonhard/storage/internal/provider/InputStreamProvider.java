package de.leonhard.storage.internal.provider;

import java.io.InputStream;

public interface InputStreamProvider {

	default InputStream createInputStreamFromInnerResource(final String resourceName) {
		return getClass().getClassLoader().getResourceAsStream(resourceName);
	}
}
