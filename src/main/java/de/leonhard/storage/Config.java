package de.leonhard.storage;

import com.esotericsoftware.yamlbeans.YamlWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
@Setter
class Config extends Yaml {

    private String[] header;
    private ConfigSettings configSettings;


    public Config(String name, String path) {
        super(name, path);
    }

    public Config(String name, String path, ReloadSettings reloadSettings) {
        super(name, path, reloadSettings);
    }

    Config(File file) {
        super(file);
    }

    @Override
    public void set(final String key, final Object value) {
        reload();


        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        final File backup = new File(file.getAbsolutePath().replace(".yml", "-changed.yml"));
        synchronized (this) {

            String old = yamlObject.toString();
            yamlObject.put(finalKey, value);

            if (old.equals(yamlObject.toString()) && yamlObject != null)
                return;
            try {
                YamlWriter writer = new YamlWriter(new FileWriter(backup));
                writer.write(yamlObject.toHashMap());
                writer.close();
                //TODO MERGE
//                Files.copy(backup.toPath(), file.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                old = null;
            }

        }


    }
}
