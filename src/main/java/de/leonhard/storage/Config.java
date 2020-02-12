package de.leonhard.storage;

import de.leonhard.storage.internal.serialize.LightningSerializer;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.ClassWrapper;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"unused"})
public class Config extends Yaml {
	private List<String> header;

	public Config(final Config config) {
		super(config);
	}

	public Config(final String name, final String path) {
		this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
	}

	public Config(final String name,
	              @Nullable final String path,
	              @Nullable final InputStream inputStream) {
		this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
	}

	public Config(final String name,
	              @Nullable final String path,
	              @Nullable final InputStream inputStream,
	              @Nullable final ReloadSettings reloadSettings,
	              @Nullable final ConfigSettings configSettings,
	              @Nullable final DataType dataType) {
		super(name, path, inputStream, reloadSettings, configSettings, dataType);
		setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to override (Points where Config is unspecific for typical FlatFiles)
	// ----------------------------------------------------------------------------------------------------

	@Override
	public final void set(final String key, final Object value) {
		super.set(key, value, getConfigSettings());
	}

	@Override
	public final void setDefault(final String key, final Object value) {
		if (!contains(key)) {
			set(key, value, getConfigSettings());
		}
	}

	@Override
	public final void setSerializable(final String key, final Object value) {
		final Object data = LightningSerializer.deserialize(value);
		set(key, data, getConfigSettings());
	}

	@Override
	public final <T> T getOrSetDefault(final String key, final T def) {
		reloadIfNeeded();
		if (!contains(key)) {
			set(key, def, getConfigSettings());
			return def;
		} else {
			final Object obj = get(key);
			return ClassWrapper.getFromDef(obj, def);
		}
	}
}