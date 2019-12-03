package de.leonhard.storage.internal.serialize;

public interface LightningSerializable<T> {
	T serialize(Object obj) throws ClassCastException;

	Class<T> getClazz();

}