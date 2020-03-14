package de.leonhard.storage.internal.provider;

import java.io.InputStream;

public abstract class InputStreamProvider {

  public InputStream createInputStreamFromInnerResource(final String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }
}
