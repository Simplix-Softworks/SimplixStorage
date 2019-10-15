package de.leonhard.storage;

import de.leonhard.storage.internal.enums.ConfigSettings;
import de.leonhard.storage.internal.enums.ReloadSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Config extends Yaml {
    private List<String> header;

    public Config(final String name, final String path) {
        super(name, path);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final ReloadSettings reloadSettings) {
        super(name, path, reloadSettings);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final File file) {
        super(file);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final String resourcefile) {
        super(name, path, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final ReloadSettings reloadSettings, final String resourcefile) {
        super(name, path, reloadSettings, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final File file, final String resourcefile) {
        super(file, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final File resourcefile) {
        super(name, path, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final ReloadSettings reloadSettings, final File resourcefile) {
        super(name, path, reloadSettings, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final File file, final File resourcefile) {
        super(file, resourcefile);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final BufferedInputStream resourceStream) {
        super(name, path, resourceStream);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final String name, final String path, final ReloadSettings reloadSettings, final BufferedInputStream resourceStream) {
        super(name, path, reloadSettings, resourceStream);
        setConfigSettings(ConfigSettings.preserveComments);
    }

    public Config(final File file, final BufferedInputStream resourceStream) {
        super(file, resourceStream);
        setConfigSettings(ConfigSettings.preserveComments);
    }


    @Override
    public void set(final String key, final Object value) {
        super.set(key, value, getConfigSettings());
    }

    @Override
    public void setDefault(final String key, final Object value) {
        if (!contains(key)) {
            set(key, value, getConfigSettings());
        }
    }

    @Override
    public <T> T getOrSetDefault(final String path, T def) {
        reload();
        if (!contains(path)) {
            set(path, def, getConfigSettings());
            return def;
        } else {
            Object obj = get(path);
            if (obj instanceof String && def instanceof Float)
                obj = Double.parseDouble((String) obj);
            else if (obj instanceof String && def instanceof Double)
                obj = Double.parseDouble((String) obj);
            else if (obj instanceof String && def instanceof Integer)
                obj = Integer.parseInt((String) obj);
            return (T) obj;
        }
    }


    public List<String> getHeader() {
        if (getConfigSettings().equals(ConfigSettings.skipComments)) {
            return new ArrayList<>();
        } else if (!shouldReload()) {
            return header;
        } else {
            try {
                return getYamlEditor().readHeader();
            } catch (IOException e) {
                System.err.println("Couldn't get header of '" + getFile().getName() + "'.");
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    public void setHeader(List<String> header) {
        List<String> tmp = new ArrayList<>();
        //Updating the values to have a comments, if someone forgets to set them
        for (final String line : header) {
            if (!line.startsWith("#")) {
                tmp.add("#" + line);
            } else {
                tmp.add(line);
            }
        }
        header = tmp;
        tmp = null;
        this.header = header;

        header.forEach(System.out::println);

        if (getFile().length() == 0) {
            try {
                getYamlEditor().write(this.header);
            } catch (IOException e) {
                System.err.println("Error while setting header of '" + getName() + "'");
                e.printStackTrace();
            }
            return;
        }

        try {
            final List<String> lines = getYamlEditor().read();

            final List<String> oldHeader = getYamlEditor().readHeader();
            final List<String> footer = getYamlEditor().readFooter();
            lines.removeAll(oldHeader);
            lines.removeAll(footer);

            Collections.reverse(lines);


            Collections.reverse(lines);

            lines.addAll(header);


            lines.addAll(footer);

            getYamlEditor().write(lines);
        } catch (final IOException e) {
            System.err.println("Exception while modifying header of '" + getName() + "'");
            e.printStackTrace();
        }

    protected Config getConfigInstance() {
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Config config = (Config) obj;
            return this.header.equals(config.header)
                    && super.equals(config.getYamlInstance());
        }
    }

}