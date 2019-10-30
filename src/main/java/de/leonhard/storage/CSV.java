package de.leonhard.storage;

import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

class CSV extends FlatFile {

	private CSV(String name, String path) {
		super(name, path, FileType.CSV);
	}

	@Override
	public void remove(String key) {

	}

	@Override
	public void set(String key, Object value) {

	}

	@Override
	public Object get(final String key) {
		return null;
	}

	@Override
	protected void forceReload() {

	}

}