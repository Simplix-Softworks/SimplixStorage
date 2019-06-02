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
    public void set(String key, Object value) {
        reload();

        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = yamlObject.toString();
            yamlObject.put(finalKey, value);

            if (old.equals(yamlObject.toString()) && yamlObject != null)
                return;

            try {
                if (configSettings.equals(ConfigSettings.preserveComments)) {

                    final List<String> lines = yamlEditor.read();
                    final List<String> comments = yamlEditor.getComments();
                    final List<String> header = yamlEditor.getHeader();
                    final List<String> footer = yamlEditor.getFooter();

                    Map<String, List<String>> parsed = YamlParser.assignCommentsToKey(lines);

                    write(yamlObject.toHashMap());
                    List<String> updated = yamlEditor.read();

                    yamlEditor.write(updateWithComments(updated, footer, header, comments, parsed));
                    return;
                }
                write(yamlObject.toHashMap());

            } catch (final IOException e) {
                System.err.println("Error while writing '" + file.getName() + "'");
            }
            old = null;
        }
    }

    @Override


    public List<String> getHeader() {

        if (configSettings.equals(ConfigSettings.skipComments))
            return new ArrayList<>();

        if (!shouldReload(reloadSettings))
            return header;
        try {
            return yamlEditor.getHeader();
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
