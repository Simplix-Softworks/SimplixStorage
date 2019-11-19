package de.leonhard.storage.internal.serialize;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LightningSerializer {
    private static final List<LightningSerializable> serializables = Collections.synchronizedList(new ArrayList<>());


    /**
     * Register a serializable to our list
     *
     * @param lightningSerializable Serializable to register
     */
    public static void registerSerializable(LightningSerializable lightningSerializable) {
        serializables.add(lightningSerializable);
    }

    public static LightningSerializable getSerializable(Class<?> clazz) {
        for (final LightningSerializable serializable : serializables) {
            if (serializable.getClazz().equals(clazz))
                return serializable;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T serialize(Object obj, Class<T> clazz) {
        final LightningSerializable serializable = getSerializable(clazz);
        if (serializable == null) {
            throw new IllegalStateException("No serializable found for '" + clazz.getSimpleName() + "'");
        }

        return (T) serializable.serialize();
    }
}
