package de.leonhard.storage;

import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.Primitive;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Config extends Yaml {
    private List<String> header;

    public Config(String name, String path) {

        super(name, path);
        this.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
    }

    public Config(String name, String path, InputStream inputStream) {
        super(name, path, inputStream);
        this.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
    }

    public Config(String name,
                  String path,
                  InputStream inputStream,
                  ReloadSettings reloadSettings,
                  ConfigSettings configSettings) {
        super(name, path, inputStream, reloadSettings, configSettings);
        this.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
    }

    @Override
    public void set(String key, Object value) {
        super.set(key, value, getConfigSettings());
    }

    @Override
    public void setDefault(String key, Object value) {
        if (!contains(key)) {
            set(key, value, getConfigSettings());
        }
    }

    @Override
    public <T> T getOrSetDefault(String key, T def) {
        reload();
        if (!contains(key)) {
            set(key, def, getConfigSettings());
            return def;
        } else {
            Object obj = get(key); //
            return Primitive.getFromDef(key, def);
        }
    }
}