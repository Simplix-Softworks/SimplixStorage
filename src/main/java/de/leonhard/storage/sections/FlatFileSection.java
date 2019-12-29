package de.leonhard.storage.sections;

import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.FlatFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class FlatFileSection implements DataStorage {
	private final FlatFile flatFile;
	@Getter
	private final String pathPrefix;

	@Override
	public Set<String> singleLayerKeySet() {
		return flatFile.singleLayerKeySet(pathPrefix);
	}

	@Override
	public Set<String> singleLayerKeySet(final String key) {
		return flatFile.singleLayerKeySet(createFinalKey(key));
	}

	@Override
	public Set<String> keySet() {
		return flatFile.keySet(pathPrefix);
	}

	@Override
	public Set<String> keySet(final String key) {
		return flatFile.keySet(createFinalKey(key));
	}

	@Override
	public void remove(final String key) {
		flatFile.remove(createFinalKey(key));
	}

	@Override
	public void set(final String key, final Object value) {
		flatFile.set(createFinalKey(key), value);
	}

	@Override
	public boolean contains(final String key) {
		return flatFile.contains(createFinalKey(key));
	}

	@Override
	public Object get(final String key) {
		return flatFile.get(createFinalKey(key));
	}

	private String createFinalKey(final String key) {
		return pathPrefix == null || pathPrefix.isEmpty() ? key : pathPrefix + "." + key;
	}
}
