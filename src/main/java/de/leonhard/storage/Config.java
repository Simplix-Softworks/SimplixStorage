package de.leonhard.storage;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "CopyConstructorMissesField"})
public class Config extends Yaml {

    private List<String> header;

    public Config(@NonNull final Config config) {
        super(config);
    }

    public Config(final @NotNull String name,
                  final String path) {
        this(name, path, null, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
    }

    public Config(final @NotNull String name,
                  @Nullable final String path,
                  @Nullable final InputStream inputStream) {
        this(name, path, inputStream, null, ConfigSettings.PRESERVE_COMMENTS, DataType.SORTED);
    }

    public Config(final @NotNull String name,
                  @Nullable final String path,
                  @Nullable final InputStream inputStream,
                  @Nullable final ReloadSettings reloadSettings,
                  @Nullable final ConfigSettings configSettings,
                  @Nullable final DataType dataType) {
        super(name, path, inputStream, reloadSettings, configSettings, dataType);
        setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
    }

    public Config(final @NotNull String name,
                  @Nullable final String path,
                  @Nullable final InputStream inputStream,
                  @Nullable final ReloadSettings reloadSettings,
                  @Nullable final ConfigSettings configSettings,
                  @Nullable final DataType dataType,
                  @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, inputStream, reloadSettings, configSettings, dataType, reloadConsumer);
        setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
    }

    public Config(final @NotNull File file) {
        super(file);
    }

    // ----------------------------------------------------------------------------------------------------
    // Method overridden from Yaml
    // ----------------------------------------------------------------------------------------------------

    @Override
    public @NotNull Config addDefaultsFromInputStream() {
        return (Config) super.addDefaultsFromInputStream();
    }

    @Override
    public @NotNull Config addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
        return (Config) super.addDefaultsFromInputStream(inputStream);
    }
}
