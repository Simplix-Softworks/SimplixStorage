package de.leonhard.storage;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.provider.InputStreamProvider;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class LightningBuilder {

    private final InputStreamProvider inputStreamProvider;
    private final String path;

    private String name;
    private @Nullable InputStream inputStream;
    private ReloadSettings reloadSettings;
    private ConfigSettings configSettings;
    private DataType dataType;

    private @Nullable Consumer<FlatFile> reloadConsumer = null;

    private LightningBuilder(final String name,
                             final String path,
                             final InputStreamProvider inputStreamProvider) {
        this.name = name;
        this.path = path;
        this.inputStreamProvider = inputStreamProvider;
    }

    // ----------------------------------------------------------------------------------------------------
    // Creating our Builder
    // ----------------------------------------------------------------------------------------------------

    public static @NotNull LightningBuilder fromPath(
            @NonNull final String name,
            @NonNull final String path) {
        return new LightningBuilder(name, path, LightningProviders.inputStreamProvider());
    }

    public static @NotNull LightningBuilder fromPath(@NonNull final Path path) {
        return fromFile(path.toFile());
    }

    public static @NotNull LightningBuilder fromFile(@NonNull final File file) {
        // File shouldn't be a directory
        Valid.checkBoolean(!file.isDirectory(), "File mustn't be a directory.",
                "Please use from Directory to use a directory",
                "This is due to Java-Internals");

        return new LightningBuilder(FileUtils.replaceExtensions(file.getName()), FileUtils.getParentDirPath(file), LightningProviders.inputStreamProvider());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static @NotNull LightningBuilder fromDirectory(@NonNull final File file) {
        Valid.checkBoolean(!file.getName().contains("."),
                "File-Name mustn't contain '.'");

        if (!file.exists()) {
            file.mkdirs();
        }

        // Will return the name of the folder as default name
        return new LightningBuilder(file.getName(), file.getAbsolutePath(), LightningProviders.inputStreamProvider());
    }

    // ----------------------------------------------------------------------------------------------------
    // Adding out settings
    // ----------------------------------------------------------------------------------------------------

    public @NotNull LightningBuilder reloadCallback(@Nullable final Consumer<FlatFile> reloadConsumer) {
        this.reloadConsumer = reloadConsumer;
        return this;
    }

    public @NotNull LightningBuilder addInputStreamFromFile(@NonNull final File file) {
        this.inputStream = FileUtils.createInputStream(file);
        return this;
    }

    public @NotNull LightningBuilder addInputStreamFromResource(@NonNull final String resource) {
        this.inputStream = this.inputStreamProvider.createInputStreamFromInnerResource(resource);
        Valid.notNull(this.inputStream, "InputStream is null.",
                "No inbuilt resource '" + resource + "' found: ");

        return this;
    }

    public @NotNull LightningBuilder setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public @NotNull LightningBuilder addInputStream(@Nullable final InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public @NotNull LightningBuilder setConfigSettings(@NonNull final ConfigSettings configSettings) {
        this.configSettings = configSettings;
        return this;
    }

    public @NotNull LightningBuilder setReloadSettings(@NonNull final ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
        return this;
    }

    public @NotNull LightningBuilder setDataType(@NonNull final DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    // ----------------------------------------------------------------------------------------------------
    // Create the objects of our FileTypes
    // ----------------------------------------------------------------------------------------------------

    public @NotNull Config createConfig() {
        return new Config(
                this.name,
                this.path,
                this.inputStream,
                this.reloadSettings,
                this.configSettings,
                this.dataType,
                reloadConsumer);
    }

    public @NotNull Yaml createYaml() {
        return new Yaml(
                this.name,
                this.path,
                this.inputStream,
                this.reloadSettings,
                this.configSettings,
                this.dataType,
                reloadConsumer);
    }

    public @NotNull Toml createToml() {
        return new Toml(
                this.name,
                this.path,
                this.inputStream,
                this.reloadSettings,
                reloadConsumer);
    }

    public @NotNull Json createJson() {
        return new Json(
                this.name,
                this.path,
                this.inputStream,
                this.reloadSettings,
                reloadConsumer);
    }
}
