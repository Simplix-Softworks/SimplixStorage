package de.leonhard.storage.internal;

import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.sections.FlatFileSection;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

@Getter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public abstract class FlatFile implements DataStorage, Comparable<FlatFile> {

    protected final @NotNull File file;
    protected final @Nullable FileType fileType;
    @Setter
    protected @Nullable ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
    protected @Nullable DataType dataType = DataType.UNSORTED;
    protected @Nullable FileData fileData;
    @Nullable
    protected Consumer<FlatFile> reloadConsumer;
    @Setter
    protected String pathPrefix;
    private long lastLoaded;

    protected FlatFile(@NonNull final String name,
                       @Nullable final String path,
                       @NonNull final FileType fileType,
                       @Nullable final Consumer<FlatFile> reloadConsumer) {
        Valid.checkBoolean(!name.isEmpty(), "Name mustn't be empty");

        this.fileType = fileType;
        this.reloadConsumer = reloadConsumer;

        if (path == null || path.isEmpty()) {
            this.file = new File(FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
        } else {
            val fixedPath = path.replace("\\", "/");
            this.file = new File(fixedPath + File.separator + FileUtils.replaceExtensions(name) + "." + fileType.getExtension());
        }
    }

    protected FlatFile(@NonNull final File file,
                       @NonNull final FileType fileType) {
        this.file = file;
        this.fileType = fileType;
        this.reloadConsumer = null;
        Valid.checkBoolean(fileType == FileType.fromExtension(file),
                "Invalid file-extension for file type: '" + fileType + "'",
                "Extension: '" + FileUtils.getExtension(file) + "'");
    }

    /**
     * This constructor should only be used to store for example YAML-LIKE data in a .db file
     *
     * <p>Therefor no validation is possible. Might be unsafe.
     */
    protected FlatFile(@NonNull final File file) {
        this.file = file;
        this.reloadConsumer = null;
        // Might be null
        this.fileType = FileType.fromFile(file);
    }

    // ----------------------------------------------------------------------------------------------------
    //  Creating our file
    // ----------------------------------------------------------------------------------------------------

    /**
     * Creates an empty .yml or .json file.
     *
     * @return true if file was created.
     */
    protected final boolean create() {
        return createFile(this.file);
    }

    @Synchronized
    private boolean createFile(final @NotNull File file) {
        if (file.exists()) {
            this.lastLoaded = System.currentTimeMillis();
            return false;
        } else {
            FileUtils.getAndMake(file);
            this.lastLoaded = System.currentTimeMillis();
            return true;
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods (Reading & Writing)
    // ----------------------------------------------------------------------------------------------------

    /**
     * Forces Re-read/load the content of our flat file Should be used to put the data from the file
     * to our FileData
     */
    protected abstract Map<String, Object> readToMap() throws IOException;

    /**
     * Write our data to file
     *
     * @param data Our data
     */
    protected abstract void write(final FileData data) throws IOException;

    protected void handleReloadException(final @NotNull IOException ioException) {
        val fileName = this.fileType == null
                ? "File"
                : this.fileType.name().toLowerCase(); // fileType might be null

        System.err.println("Exception reloading " + fileName + " '" + getName() + "'");
        System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");

        ioException.printStackTrace();
    }
    // ----------------------------------------------------------------------------------------------------
    // Overridden methods from DataStorage
    // ---------------------------------------------------------------------------------------------------->

    @Override
    @Synchronized
    public void set(final String key, final Object value) {
        reloadIfNeeded();
        val finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
        Objects.requireNonNull(this.fileData).insert(finalKey, value);
        write();
        this.lastLoaded = System.currentTimeMillis();
    }

    @Override
    public final Object get(final String key) {
        reloadIfNeeded();
        val finalKey = this.pathPrefix == null ? key : this.pathPrefix + "." + key;
        return Objects.requireNonNull(getFileData()).get(finalKey);
    }

    /**
     * Checks whether a key exists in the file
     *
     * @param key Key to check
     * @return Returned value
     */
    @Override
    public final boolean contains(final String key) {
        reloadIfNeeded();
        val finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
        return Objects.requireNonNull(this.fileData).containsKey(finalKey);
    }

    @Override
    public final @NotNull Set<String> singleLayerKeySet() {
        reloadIfNeeded();
        return Objects.requireNonNull(this.fileData).singleLayerKeySet();
    }

    @Override
    public final @NotNull Set<String> singleLayerKeySet(final @NotNull String key) {
        reloadIfNeeded();
        return Objects.requireNonNull(this.fileData).singleLayerKeySet(key);
    }

    @Override
    public final @NotNull Set<String> keySet() {
        reloadIfNeeded();
        return Objects.requireNonNull(this.fileData).keySet();
    }

    @Override
    public final @NotNull Set<String> keySet(final @NotNull String key) {
        reloadIfNeeded();
        return Objects.requireNonNull(this.fileData).keySet(key);
    }

    @Override
    @Synchronized
    public final void remove(final @NotNull String key) {
        reloadIfNeeded();
        Objects.requireNonNull(this.fileData).remove(key);
        write();
    }

    // ----------------------------------------------------------------------------------------------------
    // More advanced & FlatFile specific operations to add data.
    // ----------------------------------------------------------------------------------------------------

    /**
     * Method to insert the data of a map to our FlatFile
     *
     * @param map Map to insert.
     */
    public final void putAll(final @NotNull Map<String, Object> map) {
        Objects.requireNonNull(this.fileData).putAll(map);
        write();
    }

    /**
     * @return The data of our file as a Map<String, Object>
     */
    public final @NotNull Map<String, Object> getData() {
        return Objects.requireNonNull(getFileData()).toMap();
    }

    // For performance separated from get(String key)
    public final @NotNull List<Object> getAll(final String @NotNull ... keys) {
        final List<Object> result = new ArrayList<>();

        reloadIfNeeded();

        for (val key : keys) {
            result.add(get(key));
        }

        return result;
    }

    public void removeAll(final String @NotNull ... keys) {
        for (val key : keys) {
            Objects.requireNonNull(this.fileData).remove(key);
        }

        write();
    }

    // ----------------------------------------------------------------------------------------------------
    // Pretty nice utility methods for FlatFile's
    // ----------------------------------------------------------------------------------------------------

    public final void addDefaultsFromMap(@NonNull final Map<String, Object> mapWithDefaults) {
        addDefaultsFromFileData(new FileData(mapWithDefaults, Objects.requireNonNull(this.dataType)));
    }

    public final void addDefaultsFromFileData(@NonNull final FileData newData) {
        reloadIfNeeded();

        // Creating & setting defaults
        for (val key : newData.keySet()) {
            if (!Objects.requireNonNull(this.fileData).containsKey(key)) {
                this.fileData.insert(key, newData.get(key));
            }
        }

        write();
    }

    public final void addDefaultsFromFlatFile(@NonNull final FlatFile flatFile) {
        addDefaultsFromFileData(Objects.requireNonNull(flatFile.getFileData()));
    }

    public final @NotNull String getName() {
        return this.file.getName();
    }

    public final @NotNull String getFilePath() {
        return this.file.getAbsolutePath();
    }

    @Synchronized
    public void replace(final @NotNull CharSequence target,
                        final @NotNull CharSequence replacement)
            throws IOException {
        val lines = Files.readAllLines(this.file.toPath());
        final List<String> result = new ArrayList<>();

        for (val line : lines) {
            result.add(line.replace(target, replacement));
        }

        Files.write(this.file.toPath(), result);
    }

    public void write() {
        try {
            write(this.fileData);
        } catch (final @NotNull IOException ex) {
            System.err.println("Exception writing to file '" + getName() + "'");
            System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");
            ex.printStackTrace();
        }
        this.lastLoaded = System.currentTimeMillis();
    }

    public final boolean hasChanged() {
        return FileUtils.hasChanged(this.file, this.lastLoaded);
    }

    public final void forceReload() {
        if (reloadConsumer != null) reloadConsumer.accept(this);
        Map<String, Object> out = new HashMap<>();

        try {
            out = readToMap();
        } catch (final @NotNull IOException ex) {
            handleReloadException(ex);
        } finally {
            if (this.fileData == null) {
                this.fileData = new FileData(out, Objects.requireNonNull(this.dataType));
            } else {
                this.fileData.loadData(out);
            }
            this.lastLoaded = System.currentTimeMillis();
        }
    }

    public final void clear() {
        Objects.requireNonNull(this.fileData).clear();
        write();
    }

    // ----------------------------------------------------------------------------------------------------
    // Internal stuff
    // ----------------------------------------------------------------------------------------------------

    protected final void reloadIfNeeded() {
        if (shouldReload()) forceReload();
    }

    // Should the file be re-read before the next get() operation?
    // Can be used as utility method for implementations of FlatFile
    protected boolean shouldReload() {
        if (ReloadSettings.AUTOMATICALLY.equals(this.reloadSettings)) {
            return true;
        } else if (ReloadSettings.INTELLIGENT.equals(this.reloadSettings)) {
            return FileUtils.hasChanged(this.file, this.lastLoaded);
        } else {
            return false;
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Misc
    // ----------------------------------------------------------------------------------------------------

    public final @Nullable FileData getFileData() {
        Valid.notNull(this.fileData, "FileData mustn't be null");
        return this.fileData;
    }

    public final @NotNull FlatFileSection getSection(final String pathPrefix) {
        return new FlatFileSection(this, pathPrefix);
    }

    @Override
    public final int compareTo(@NonNull final FlatFile flatFile) {
        return this.file.compareTo(flatFile.file);
    }
}
