package de.leonhard.storage.internal.serialize;

import de.leonhard.storage.util.Valid;
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
		Valid.notNull(lightningSerializable, "Serializable mustn't be null");
		Valid.notNull(lightningSerializable.getClazz(), "Class mustn't be null");
		serializes.add(lightningSerializable);
	}

	public LightningSerializable findSerializable(Class<?> clazz) {
		for (LightningSerializable serializable : serializes) {
			if (serializable.getClazz().equals(clazz)) {
				return serializable;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T serialize(Object obj, Class<T> clazz) {
		LightningSerializable serializable = findSerializable(clazz);
		Valid.checkBoolean(serializable != null, "No serializable found for '" + clazz.getSimpleName() + "'");
		return (T) serializable.deserialize(obj);
	}

	@SuppressWarnings("unchecked")
	public Object deserialize(Object obj) {
		LightningSerializable serializable = findSerializable(obj.getClass());
		Valid.checkBoolean(serializable != null, "No serializable found for '" + obj.getClass().getSimpleName() + "'");
		return serializable.serialize(obj);
	}
}
