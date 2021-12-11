package de.leonhard.storage.internal.provider;

import java.io.InputStream;
import org.jetbrains.annotations.Nullable;

public abstract class InputStreamProvider {

  @Nullable
  public InputStream createInputStreamFromInnerResource(final String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }
}
