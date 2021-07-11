package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.toml.TomlManager;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

public class Toml extends FlatFile {

    public Toml(@NonNull final Toml toml) {
        super(toml.getFile());
        this.fileData = toml.getFileData();
        this.pathPrefix = toml.getPathPrefix();
    }

    public Toml(final @NotNull String name, final @NotNull String path) {
        this(name, path, null);
    }

    public Toml(final @NotNull String name, final @NotNull String path, final InputStream inputStream) {
        this(name, path, inputStream, null, null);
    }

    public Toml(@NonNull final String name,
                @NonNull final String path,
                @Nullable final InputStream inputStream,
                @Nullable final ReloadSettings reloadSettings,
                @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, FileType.TOML, reloadConsumer);

        if (create() && inputStream != null) {
            FileUtils.writeToFile(this.file, inputStream);
        }

        if (reloadSettings != null) {
            this.reloadSettings = reloadSettings;
        }

        forceReload();
    }

    public Toml(final @NotNull File file) {
        super(file, FileType.TOML);
        create();
        forceReload();
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected final @NotNull Map<String, Object> readToMap() throws IOException {
        return TomlManager.read(getFile());
    }

    @Override
    protected final void write(final @NotNull FileData data) {
        try {
            TomlManager.write(data.toMap(), getFile());
        } catch (final @NotNull IOException ioException) {
            System.err.println("Exception while writing fileData to file '" + getName() + "'");
            System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");
            ioException.printStackTrace();
        }
    }
}
