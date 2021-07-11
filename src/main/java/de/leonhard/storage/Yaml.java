package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.editor.yaml.SimpleYamlReader;
import de.leonhard.storage.internal.editor.yaml.SimpleYamlWriter;
import de.leonhard.storage.internal.editor.yaml.YamlEditor;
import de.leonhard.storage.internal.editor.yaml.YamlParser;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class Yaml extends FlatFile {

    protected final @Nullable InputStream inputStream;
    protected final YamlEditor yamlEditor;
    protected final YamlParser parser;

    @Setter
    private @Nullable ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;

    public Yaml(@NonNull final Yaml yaml) {
        super(yaml.getFile());
        this.fileData = yaml.getFileData();
        this.yamlEditor = yaml.getYamlEditor();
        this.parser = yaml.getParser();
        this.configSettings = yaml.getConfigSettings();
        this.inputStream = yaml.getInputStream().orElse(null);
        this.pathPrefix = yaml.getPathPrefix();
        this.reloadConsumer = yaml.getReloadConsumer();
    }

    public Yaml(final @NotNull String name,
                @Nullable final String path) {
        this(name, path, null, null, null, null);
    }

    public Yaml(final @NotNull String name,
                @Nullable final String path,
                @Nullable final InputStream inputStream) {
        this(name, path, inputStream, null, null, null);
    }

    public Yaml(final @NotNull String name,
                @Nullable final String path,
                @Nullable final InputStream inputStream,
                @Nullable final ReloadSettings reloadSettings,
                @Nullable final ConfigSettings configSettings,
                @Nullable final DataType dataType) {
        this(name, path, inputStream, reloadSettings, configSettings, dataType, null);
    }

    public Yaml(final @NotNull String name,
                @Nullable final String path,
                @Nullable final InputStream inputStream,
                @Nullable final ReloadSettings reloadSettings,
                @Nullable final ConfigSettings configSettings,
                @Nullable final DataType dataType,
                @Nullable final Consumer<FlatFile> reloadConsumer) {
        super(name, path, FileType.YAML, reloadConsumer);
        this.inputStream = inputStream;

        if (create() && inputStream != null) {
            FileUtils.writeToFile(this.file, inputStream);
        }

        this.yamlEditor = new YamlEditor(this.file);
        this.parser = new YamlParser(this.yamlEditor);

        if (reloadSettings != null) {
            this.reloadSettings = reloadSettings;
        }

        if (configSettings != null) {
            this.configSettings = configSettings;
        }

        if (dataType != null) {
            this.dataType = dataType;
        } else {
            this.dataType = DataType.forConfigSetting(configSettings);
        }

        forceReload();
    }

    public Yaml(final @NotNull File file) {
        this(file.getName(), FileUtils.getParentDirPath(file));
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods to override (Points where YAML is unspecific for typical FlatFiles)
    // ----------------------------------------------------------------------------------------------------

    public Yaml addDefaultsFromInputStream() {
        return addDefaultsFromInputStream(getInputStream().orElse(null));
    }

    public Yaml addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
        reloadIfNeeded();

        // Creating & setting defaults
        if (inputStream == null) {
            return this;
        }

        try {
            val data = new SimpleYamlReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readToMap();
            val newData = new FileData(data, DataType.UNSORTED);

            for (val key : newData.keySet()) {
                if (!Objects.requireNonNull(this.fileData).containsKey(key)) {
                    this.fileData.insert(key, newData.get(key));
                }
            }

            write();
        } catch (final @NotNull Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected @NotNull Map<String, Object> readToMap() throws IOException {
        @Cleanup val reader = new SimpleYamlReader(new FileReader(getFile()));
        return reader.readToMap();
    }

    @Override
    protected void write(final FileData data) throws IOException {
        // If Comments shouldn't be preserved
        if (!ConfigSettings.PRESERVE_COMMENTS.equals(this.configSettings)) {
            write0(Objects.requireNonNull(this.fileData));
            return;
        }

        val unEdited = this.yamlEditor.read();
        write0(Objects.requireNonNull(this.fileData));
        this.yamlEditor.write(this.parser.parseLines(unEdited, this.yamlEditor.readKeys()));
    }

    // Writing without comments
    private void write0(final @NotNull FileData fileData) throws IOException {
        @Cleanup val writer = new SimpleYamlWriter(this.file);
        writer.write(fileData.toMap());
    }

    // ----------------------------------------------------------------------------------------------------
    // Specific utility methods for YAML
    // ----------------------------------------------------------------------------------------------------

    public final @NotNull List<String> getHeader() {
        return this.yamlEditor.readHeader();
    }

    public final void setHeader(final @NotNull List<String> header) {
        this.yamlEditor.setHeader(header);
    }

    public final void setHeader(final String... header) {
        setHeader(Arrays.asList(header));
    }

    public final void addHeader(final @NotNull List<String> toAdd) {
        this.yamlEditor.addHeader(toAdd);
    }

    @SuppressWarnings("unused")
    public final void addHeader(final String... header) {
        addHeader(Arrays.asList(header));
    }

    @SuppressWarnings("unused")
    public final void framedHeader(final String @NotNull ... header) {
        List<String> stringList = new ArrayList<>();
        var border = "# +----------------------------------------------------+ #";
        stringList.add(border);

        for (String line : header) {
            var builder = new StringBuilder();
            if (line.length() > 50) continue;

            int length = (50 - line.length()) / 2;
            var finalLine = new StringBuilder(line);

            for (int i = 0; i < length; i++) {
                finalLine.append(" ");
                finalLine.reverse();
                finalLine.append(" ");
                finalLine.reverse();
            }

            if (line.length() % 2 != 0) {
                finalLine.append(" ");
            }

            builder.append("# < ").append(finalLine).append(" > #");
            stringList.add(builder.toString());
        }
        stringList.add(border);
        setHeader(stringList);
    }

    public final Optional<InputStream> getInputStream() {
        return Optional.ofNullable(this.inputStream);
    }
}
