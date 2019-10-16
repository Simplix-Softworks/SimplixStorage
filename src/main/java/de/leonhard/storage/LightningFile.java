package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class LightningFile extends FlatFile implements StorageBase {
    @Setter
    private String pathPrefix;
    private FileData fileData;
    private PrintWriter writer;

    public LightningFile(final String name, final String path) {
        create(name, path, FileType.LS);
    }

    public LightningFile(final String name, final String path, final ReloadSettings reloadSettings) {
        this.reloadSettings = reloadSettings;
    }

    LightningFile(final File file) {
        create(file);
    }

    //added method for later implementation
    @Override
    public void update() {
        //TODO
    }

    //added method for later implementation
    @Override
    public Set<String> singleLayerKeySet() {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> singleLayerKeySet(final String key) {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> keySet() {
        return null;
    }

    //added method for later implementation
    @Override
    public Set<String> keySet(final String key) {
        return null;
    }

    //added method for later implementation
    @Override
    public void remove(final String key) {
        //TODO
    }

    //added method for later implementation
    @Override
    public void set(final String key, final Object value) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = fileData.toString();
            fileData.insert(finalKey, value);

            if (fileData != null && old.equals(fileData.toString())) {
                return;
            }
            try {
                writer = new PrintWriter(file);
                write(fileData.toMap(), "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(Map<String, Object> map, String indentation) {
        writer.println(indentation + "{");
        for (String key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                //noinspection unchecked
                write((Map<String, Object>) map.get(key), indentation + "  ");
            } else {
                writer.println(indentation + key + " = " + map.get(key));
            }
        }
        writer.println(indentation + "}");
    }

    private void read() {
        try {
            HashMap<String, Object> map = new HashMap<>();
            String[] lines = FileUtils.readAllLines(this.file).toArray(new String[0]);
            for (int i = 0; i < lines.length; i++) {
                while (lines[i].startsWith(" ")) {
                    lines[i] = lines[i].substring(1);
                }
                while (lines[i].endsWith(" ")) {
                    lines[i] = lines[i].substring(0, lines.length - 1);
                }
                if (lines[i].endsWith("{")) {
                    HashMap<String, Object> tempmap = new HashMap<>();
                    String key = lines[i];
                    i = read(tempmap, lines, i);
                    map.put(key.replace("{", ""), tempmap);
                } else {
                    String[] line = lines[i].split(" = ");
                    map.put(line[0], line[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int read(HashMap<String, Object> map, String[] lines, int id) {
        for (int i = id; i < lines.length; i++) {
            while (lines[i].startsWith(" ")) {
                lines[i] = lines[i].substring(1);
            }
            while (lines[i].endsWith(" ")) {
                lines[i] = lines[i].substring(0, lines.length - 1);
            }
            if (lines[i].endsWith("{")) {
                HashMap<String, Object> tempmap = new HashMap<>();
                String key;
                if (lines[i].equals("{")) {
                    key = lines[i - 1];
                } else {
                    key = lines[i].replace("{", "");
                }
                i = read(tempmap, lines, i);
                map.put(key, tempmap);
            } else if (lines[i].equals("}")) {
                return i;
            } else {
                String[] line = lines[i].split(" = ");
                map.put(line[0], line[1]);
            }
        }
        return lines.length - 1;
    }

    @Override
    public boolean contains(final String key) {
        return false;
    }

    @Override
    public Object get(final String key) {
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            LightningFile lightningFile = (LightningFile) obj;
            return this.fileData.equals(lightningFile.fileData)
                    && super.equals(lightningFile.getFlatFileInstance());
        }
    }
}