package de.leonhard.storage.base;

import de.leonhard.storage.ConfigSettings;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface YamlBase extends StorageBase {

    void write(Map data) throws IOException;

    List<String> getHeader();

    void set(final String key, final Object value, final ConfigSettings configSettings);


}
