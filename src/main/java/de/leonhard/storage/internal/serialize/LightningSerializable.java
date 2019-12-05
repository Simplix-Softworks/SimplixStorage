package de.leonhard.storage.internal.serialize;

public interface LightningSerializable<T> {

	/**
	 * Get our serializable from data in data-structure.
	 *
	 * @param obj Data to deserialize our class from.
	 * @throws ClassCastException Exception thrown when deserialization failed.
	 */
	T deserialize(Object obj) throws ClassCastException;

	/**
	 * Save our serializable to data-structure.
	 *
	 * @throws ClassCastException Exception thrown when serialization failed.
	 */
	Object serialize() throws ClassCastException;

	Class<T> getClazz();
}