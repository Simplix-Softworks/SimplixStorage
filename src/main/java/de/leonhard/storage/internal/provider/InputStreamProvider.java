package de.leonhard.storage.internal.provider;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public abstract class InputStreamProvider {

    @Nullable
    public InputStream createInputStreamFromInnerResource(final String resourceName) {
        return getClass().getClassLoader().getResourceAsStream(resourceName);
    }
}
