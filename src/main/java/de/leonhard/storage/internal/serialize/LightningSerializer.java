package de.leonhard.storage.internal.serialize;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to register serializable's
 */
@UtilityClass
public class LightningSerializer {
	private final List<LightningSerializable> serializes = Collections.synchronizedList(new ArrayList<>());

	/**
	 * Register a serializable to our list
	 *
	 * @param lightningSerializable Serializable to register
	 */
	public void registerSerializable(LightningSerializable lightningSerializable) {
		serializes.add(lightningSerializable);
	}

	public LightningSerializable getSerializable(Class<?> clazz) {
		for (LightningSerializable serializable : serializes) {
			if (serializable.getClazz().equals(clazz)) {
				return serializable;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T serialize(Object obj, Class<T> clazz) {
		LightningSerializable serializable = getSerializable(clazz);
		if (serializable == null) {
			throw new IllegalStateException("No serializable found for '" + clazz.getSimpleName() + "'");
		}

		return (T) serializable.serialize(obj);
	}
}
