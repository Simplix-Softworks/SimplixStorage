package de.leonhard.storage.base;

import java.util.Map;

public interface TomlBase extends StorageBase {

    void write(final Map<String, Object> data);

}
