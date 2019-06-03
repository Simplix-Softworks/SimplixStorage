package de.leonhard.storage;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
class Config extends Yaml {


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

    public void setHeader(List<String> header) {

        if (configSettings.equals(ConfigSettings.skipComments))
            return;

        final List<String> oldHeader = getHeader();
        try {
            final List<String> lines = yamlEditor.read();

//            lines.removeAll(oldHeader);

            for (final String head : oldHeader) {
                lines.add(oldHeader.indexOf(head), head);
            }


            lines.forEach(System.out::println);

            for (final String head : header) {
                lines.add(header.indexOf(head), head);
            }


            lines.forEach(System.out::println);

            yamlEditor.write(lines);

        } catch (IOException e) {
            System.err.println("Couldn't set header of '" + file.getName() + "'");
            e.printStackTrace();
        }

        this.header = header;


    }
}
