package de.leonhard.storage;

import de.leonhard.storage.base.ConfigBase;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class Config extends Yaml implements ConfigBase {


    private List<String> header;
    private ConfigSettings configSettings;

    public Config(String name, String path) {
        super(name, path);
        this.configSettings = ConfigSettings.preserveComments;
    }

    public Config(String name, String path, ReloadSettings reloadSettings) {
        super(name, path, reloadSettings);
        this.configSettings = ConfigSettings.preserveComments;
    }

    Config(File file) {
        super(file);
        this.configSettings = ConfigSettings.preserveComments;
    }

    @Override
    public void set(String key, Object value) {
        super.set(key, value, configSettings);
    }

    @Override
    public void setDefault(final String key, final Object value) {
        if (!contains(key))
            set(key, value, configSettings);
    }

    @Override
    public <T> T getOrSetDefault(final String path, T def) {
        reload();
        if (!contains(path)) {
            set(path, def, configSettings);
            return def;
        } else {
            Object obj = get(path); //
            if (obj instanceof String && def instanceof Integer)
                obj = Integer.parseInt((String) obj);
            if (obj instanceof String && def instanceof Double)
                obj = Double.parseDouble((String) obj);
            if (obj instanceof String && def instanceof Float)
                obj = Double.parseDouble((String) obj);
            return (T) obj;
        }
    }


    @Override
    public List<String> getHeader() {

        if (configSettings.equals(ConfigSettings.skipComments))
            return new ArrayList<>();

        if (!shouldReload(reloadSettings))
            return header;
        try {
            return yamlEditor.readHeader();
        } catch (IOException e) {
            System.err.println("Couldn't get header of '" + file.getName() + "'.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void setHeader(List<String> header) {
        List<String> tmp = new ArrayList<>();
        //Updating the values to have a comments, if someone forgets to set them
        for (final String line : header) {
            if (!line.startsWith("#"))
                tmp.add("#" + line);
            else
                tmp.add(line);
        }
        header = tmp;
        tmp = null;
        this.header = header;

        header.forEach(System.out::println);

        if (file.length() == 0) {
            try {
                yamlEditor.write(header);
            } catch (IOException e) {
                System.err.println("Error while setting header of '" + file.getName() + "'");
                e.printStackTrace();
            }
            return;
        }

        try {

            final List<String> lines = yamlEditor.read();

            final List<String> oldHeader = yamlEditor.readHeader();
            final List<String> footer = yamlEditor.readFooter();
            lines.removeAll(oldHeader);
            lines.removeAll(footer);

            Collections.reverse(lines);


            Collections.reverse(lines);

            lines.addAll(header);


            lines.addAll(footer);


            yamlEditor.write(lines);

        } catch (final IOException e) {
            System.err.println("Exception while modifying header of '" + file.getName() + "'");
            e.printStackTrace();
        }


    }

}
