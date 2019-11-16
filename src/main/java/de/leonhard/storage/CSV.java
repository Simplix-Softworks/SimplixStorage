package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;

class CSV extends FlatFile {
	private CSV(String name, String path) {
		super(name, path, FileType.CSV);
	}

	@Override
	protected void reRead() {

	}

	@Override
	protected void write(FileData data) {

	}
}