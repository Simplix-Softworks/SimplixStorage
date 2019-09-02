package de.leonhard.storage.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface YamlBase extends StorageBase {

    void write(Map data) throws IOException;

    List<String> getHeader();

    void set(final String key, final Object value, final ConfigSettings configSettings);


}
