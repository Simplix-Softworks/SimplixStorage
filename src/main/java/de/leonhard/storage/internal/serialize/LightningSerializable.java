package de.leonhard.storage.internal.serialize;

public interface LightningSerializable<T> {
    T serialize() throws ClassCastException;

    Class<T> getClazz();
}