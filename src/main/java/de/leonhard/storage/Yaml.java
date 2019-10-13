package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.base.*;
import de.leonhard.storage.editor.YamlEditor;
import de.leonhard.storage.editor.YamlParser;
import de.leonhard.storage.util.FileData;
import de.leonhard.storage.util.FileUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
@Getter
@Setter
public class Yaml extends StorageCreator implements StorageBase, Comparable<Yaml> {
    protected final YamlEditor yamlEditor;
    protected final YamlParser parser;
    protected File file;
    @Setter(AccessLevel.PRIVATE)
    protected FileData fileData;
    protected String pathPrefix;
    protected ReloadSettings reloadSettings = ReloadSettings.INTELLIGENT;
    protected ConfigSettings configSettings = ConfigSettings.skipComments;
    private BufferedInputStream configInputStream;
    private FileOutputStream outputStream;


    public Yaml(String name, String path) {
        try {
            create(path, name, FileType.YAML);
            this.file = super.file;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        this.reloadSettings = ReloadSettings.INTELLIGENT;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings) {
        try {
            create(path, name, FileType.YAML);
            this.file = super.file;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        update();
    }

    public Yaml(final File file) {
        if (isYaml(file)) {
            try {
                create(file);
                this.file = super.file;
            } catch (final IOException e) {
                e.printStackTrace();
            }
            load(file);

            update();

            yamlEditor = new YamlEditor(file);
            parser = new YamlParser(yamlEditor);
        } else {
            yamlEditor = null;
            parser = null;
        }
    }

    public Yaml(String name, String path, String resourcefile) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;
                try {
                    this.configInputStream = new BufferedInputStream(
                            Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile + ".yml")));
                    outputStream = new FileOutputStream(this.file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings, String resourcefile) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;
                try {
                    this.configInputStream = new BufferedInputStream(
                            Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile + ".yml")));
                    outputStream = new FileOutputStream(file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        update();
    }

    public Yaml(final File file, String resourcefile) {
        if (isYaml(file)) {
            try {
                if (create(file)) {
                    this.file = super.file;

                    try {
                        this.configInputStream = new BufferedInputStream(
                                Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile + ".yml")));
                        outputStream = new FileOutputStream(this.file);
                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                            outputStream.write(data, 0, count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (this.configInputStream != null) {
                            this.configInputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                } else {
                    this.file = super.file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            load(file);

            update();

            yamlEditor = new YamlEditor(file);
            parser = new YamlParser(yamlEditor);
        } else {
            yamlEditor = null;
            parser = null;
        }
    }

    public Yaml(String name, String path, File resourcefile) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;

                try {
                    this.configInputStream = new BufferedInputStream(
                            Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile.getName())));
                    outputStream = new FileOutputStream(this.file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings, File resourcefile) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;

                try {
                    this.configInputStream = new BufferedInputStream(
                            Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile.getName())));
                    outputStream = new FileOutputStream(this.file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        update();
    }

    public Yaml(final File file, File resourcefile) {
        if (isYaml(file)) {
            try {
                if (create(file)) {
                    this.file = super.file;

                    try {
                        this.configInputStream = new BufferedInputStream(
                                Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream(resourcefile.getName())));
                        outputStream = new FileOutputStream(this.file);
                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                            outputStream.write(data, 0, count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (this.configInputStream != null) {
                            this.configInputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                } else {
                    this.file = super.file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            load(file);

            update();

            yamlEditor = new YamlEditor(file);
            parser = new YamlParser(yamlEditor);
        } else {
            yamlEditor = null;
            parser = null;
        }
    }

    public Yaml(String name, String path, BufferedInputStream resourceStream) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;

                try {
                    this.configInputStream = resourceStream;
                    outputStream = new FileOutputStream(this.file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);
        update();
    }

    public Yaml(String name, String path, ReloadSettings reloadSettings, BufferedInputStream resourceStream) {
        try {
            if (create(path, name, FileType.YAML)) {
                this.file = super.file;

                try {
                    this.configInputStream = resourceStream;
                    outputStream = new FileOutputStream(this.file);
                    final byte[] data = new byte[1024];
                    int count;
                    while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (this.configInputStream != null) {
                        this.configInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } else {
                this.file = super.file;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.reloadSettings = reloadSettings;
        yamlEditor = new YamlEditor(file);
        parser = new YamlParser(yamlEditor);

        update();
    }

    public Yaml(final File file, BufferedInputStream resourceStream) {
        if (isYaml(file)) {
            try {
                if (create(file)) {
                    this.file = super.file;

                    try {
                        this.configInputStream = resourceStream;
                        outputStream = new FileOutputStream(this.file);
                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = this.configInputStream.read(data, 0, 1024)) != -1) {
                            outputStream.write(data, 0, count);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (this.configInputStream != null) {
                            this.configInputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                } else {
                    this.file = super.file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            load(file);

            update();

            yamlEditor = new YamlEditor(file);
            parser = new YamlParser(yamlEditor);
        } else {
            yamlEditor = null;
            parser = null;
        }
    }

    public static boolean isYaml(String file) {
        return (file.lastIndexOf(".") > 0 ? file.substring(file.lastIndexOf(".") + 1) : "").equals("yml");
    }

    public static boolean isYaml(File file) {
        return isYaml(file.getName());
    }

    public String getName() {
        return this.file.getName();
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public void set(String key, Object value) {
        insert(key, value, this.configSettings);
    }

    public void set(String key, Object value, ConfigSettings configSettings) {
        insert(key, value, configSettings);
    }

    private void insert(String key, Object value, ConfigSettings configSettings) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = fileData.toString();
            fileData.insert(finalKey, value);

            if (fileData != null && old.equals(fileData.toString())) {
                return;
            }

            try {
                if (configSettings.equals(ConfigSettings.preserveComments)) {

                    final List<String> unEdited = yamlEditor.read();
                    final List<String> header = yamlEditor.readHeader();
                    final List<String> footer = yamlEditor.readFooter();
                    write(fileData.toMap());
                    header.addAll(yamlEditor.read());
                    if (!header.containsAll(footer))
                        header.addAll(footer);
                    yamlEditor.write(parser.parseComments(unEdited, header));
                    return;
                }
                write(Objects.requireNonNull(fileData).toMap());
            } catch (final IOException e) {
                System.err.println("Error while writing '" + file.getName() + "'");
            }
        }
    }

    public void write(Map data) throws IOException {
        YamlWriter writer = new YamlWriter(new FileWriter(file));
        writer.write(data);
        writer.close();
    }

    @Override
    public Object get(final String key) {
        reload();
        String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
        return fileData.get(key);
    }

    @Override
    public boolean contains(String key) {
        key = (pathPrefix == null) ? key : pathPrefix + "." + key;
        return has(key);
    }

    private boolean has(String key) {
        reload();

        return fileData.containsKey(key);
    }

    protected void reload() {
        if (reloadSettings.equals(ReloadSettings.MANUALLY)) {
            return;
        }

        if (ReloadSettings.INTELLIGENT.equals(reloadSettings)) {
            if (FileUtils.hasNotChanged(file, lastModified)) {
                return;
            }
        }

        update();
    }

    public boolean hasNotChanged() {
        return FileUtils.hasNotChanged(file, lastModified);
    }

    @Override
    public void update() {
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader(file));// Needed?
            fileData = new FileData((Map<String, Object>) reader.read());
        } catch (IOException e) {
            System.err.println("Exception while reloading yaml");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Exception while closing file");
                e.printStackTrace();
            }
        }
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
        reload();
    }

    public List<String> getHeader() {
        try {
            return yamlEditor.readHeader();
        } catch (IOException e) {
            System.err.println("Error while getting header of '" + file.getName() + "'");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        fileData.remove(finalKey);

        try {
            write(fileData.toMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> keySet() {
        reload();
        return fileData.keySet();
    }

    @Override
    public Set<String> keySet(String key) {
        reload();
        return fileData.keySet(key);
    }

    @Override
    public Set<String> deepKeySet() {
        reload();
        return fileData.deepKeySet();
    }

    @Override
    public Set<String> deepKeySet(String key) {
        reload();
        return fileData.deepKeySet(key);
    }

    public void setConfigSettings(final ConfigSettings configSettings) {
        this.configSettings = configSettings;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            Yaml yaml = (Yaml) obj;
            return this.file.equals(yaml.file);
        } else {
            return false;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Yaml yaml) {
        return this.file.compareTo(yaml.file);
    }

    @Override
    public int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public String toString() {
        return this.file.getAbsolutePath();
    }
}
