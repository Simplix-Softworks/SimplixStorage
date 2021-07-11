package de.leonhard.storage.sections;

import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.FlatFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@RequiredArgsConstructor
public class FlatFileSection implements DataStorage {

    protected final @NotNull FlatFile flatFile;
    @Getter
    private final @NotNull String pathPrefix;

    @Override
    public @NotNull Set<String> singleLayerKeySet() {
        return flatFile.singleLayerKeySet(pathPrefix);
    }

    @Override
    public @NotNull Set<String> singleLayerKeySet(final @NotNull String key) {
        return flatFile.singleLayerKeySet(createFinalKey(key));
    }

    @Override
    public @NotNull Set<String> keySet() {
        return flatFile.keySet(pathPrefix);
    }

    @Override
    public @NotNull Set<String> keySet(final @NotNull String key) {
        return flatFile.keySet(createFinalKey(key));
    }

    @Override
    public void remove(final @NotNull String key) {
        flatFile.remove(createFinalKey(key));
    }

    @Override
    public void set(final @NotNull String key, final Object value) {
        flatFile.set(createFinalKey(key), value);
    }

    @Override
    public boolean contains(final @NotNull String key) {
        return flatFile.contains(createFinalKey(key));
    }

    @Override
    public Object get(final @NotNull String key) {
        return flatFile.get(createFinalKey(key));
    }

    @Override
    public <E extends Enum<E>> E getEnum(@NotNull String key, @NotNull Class<E> enumType) {
        return flatFile.getEnum(createFinalKey(key), enumType);
    }

    private @NotNull String createFinalKey(final @NotNull String key) {
        return pathPrefix.isEmpty() ? key : pathPrefix + "." + key;
    }
}
