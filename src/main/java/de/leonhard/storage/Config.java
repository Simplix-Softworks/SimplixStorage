package de.leonhard.storage;

import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.ClassWrapper;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"unused"})
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Config extends Yaml {
	private List<String> header;


	public Config(String name, String path) {
		this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
	}

	public Config(String name, String path, InputStream inputStream) {
		this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
	}

	public Config(String name,
	              String path,
	              InputStream inputStream,
	              ReloadSettings reloadSettings,
	              ConfigSettings configSettings,
	              DataType dataType) {
		super(name, path, inputStream, reloadSettings, configSettings, dataType);
		this.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to override (Points where Config is unspecific for typical FlatFiles)
	// ----------------------------------------------------------------------------------------------------

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
		reloadIfNeeded();
		if (!contains(key)) {
			set(key, def, getConfigSettings());
			return def;
		} else {
			Object obj = get(key);
			return ClassWrapper.getFromDef(key, def);
		}
	}
}