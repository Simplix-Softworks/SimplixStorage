package de.leonhard.storage.base;

import de.leonhard.storage.ConfigSettings;

public interface ConfigBase extends YamlBase {


    void set(final String key, final Object value, final ConfigSettings configSettings);


}
