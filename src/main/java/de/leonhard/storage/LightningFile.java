package de.leonhard.storage;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.StorageBase;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@Getter
public class LightningFile extends FlatFile implements StorageBase {
    @Setter
    private String pathPrefix;
    private PrintWriter writer;

    public LightningFile(final String name, final String path) {
        create(name, path, FileType.LS);
    }

    public LightningFile(final String name, final String path, final ReloadSettings reloadSettings) {
        create(name, path, FileType.LS);
        this.reloadSettings = reloadSettings;
    }

    public LightningFile(final File file) {
        create(file);
    }

    //added method for later implementation
    @Override
    public void update() {
        try {
            this.fileData = new FileData(read());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Map<String, Object> map = fileData.toMap();
                for (String localKey : map.keySet()) {
                    if (localKey.startsWith("#")) {
                        writer.println("  " + localKey);
                    } else if (map.get(localKey) instanceof Map) {
                        writer.println(key + " " + "{");
                        //noinspection unchecked
                        write((Map<String, Object>) map.get(localKey), "  ");
                    } else {
                        writer.println("  " + localKey + " = " + map.get(localKey));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(Map<String, Object> map, String indentationString) {
        for (String key : map.keySet()) {
            if (key.startsWith("#")) {
                writer.println(indentationString + "  " + key);
            } else if (map.get(key) instanceof Map) {
                writer.println(indentationString + key + " " + "{");
                //noinspection unchecked
                write((Map<String, Object>) map.get(key), indentationString + "  ");
            } else {
                writer.println(indentationString + "  " + key + " = " + map.get(key));
            }
        }
        writer.println(indentationString + "}");
    }

    @SuppressWarnings("Duplicates")
    private Map<String, Object> read() throws IOException {
        Map<String, Object> tempMap = new HashMap<>();
        String tempKey = null;
        List<String> lines = FileUtils.readAllLines(this.file);
        while (lines.size() > 0) {
            while (lines.get(0).startsWith(" ")) {
                lines.set(0, lines.get(0).substring(1));
            }
            while (lines.get(0).endsWith(" ")) {
                lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            }

            if (lines.get(0).contains("}")) {
                throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
            } else if (lines.get(0).startsWith("#")) {
                tempMap.put(lines.get(0), null);
            } else if (lines.get(0).endsWith("{")) {
                if (!lines.get(0).equals("{")) {
                    tempKey = lines.get(0).replace("{", "");
                    while (tempKey.endsWith(" ")) {
                        tempKey = tempKey.substring(0, tempKey.length() - 1);
                    }
                } else if (tempKey == null) {
                    throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
                }
                lines.remove(0);
                tempMap.put(tempKey, read(lines));
            } else {
                if (lines.get(0).contains(" = ")) {
                    String[] line = lines.get(0).split(" = ");
                    lines.remove(0);
                    tempMap.put(line[0], line[1]);
                } else {
                    if (lines.get(1).contains("{")) {
                        tempKey = lines.get(0);
                    } else {
                        throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
                    }
                }
            }
        }
            /*for (int i = 0; i < lines.size(); i++) {
                while (lines.get(i).startsWith(" ")) {
                    lines.set(i, lines.get(i).substring(1));
                }
                while (lines.get(i).endsWith(" ")) {
                    lines.set (i, lines.get(i).substring(0, lines.get(i).length() - 1));
                }
                if (lines.get(i).endsWith("{")) {
                    HashMap<String, Object> tempmap = new HashMap<>();
                    String key = lines.get(i);
                    i = read(tempmap, lines, i);
                    map.put(key.replace("{", ""), tempmap);
                } else {
                    String[] line = lines.get(i).split(" = ");
                    map.put(line[0], line[1]);
                }
            }*/
        return tempMap;
    }

    @SuppressWarnings("Duplicates")
    private Map<String, Object> read(List<String> lines) throws InvalidObjectException {
        Map<String, Object> tempMap = new HashMap<>();
        String tempKey = null;
        while (lines.size() > 0) {
            while (lines.get(0).startsWith(" ")) {
                lines.set(0, lines.get(0).substring(1));
            }
            while (lines.get(0).endsWith(" ")) {
                lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            }

            if (lines.get(0).equals("}")) {
                return tempMap;
            } else if (lines.get(0).contains("}")) {
                throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
            } else if (lines.get(0).startsWith("#")) {
                tempMap.put(lines.get(0), null);
            } else if (lines.get(0).endsWith("{")) {
                if (!lines.get(0).equals("{")) {
                    tempKey = lines.get(0).replace("{", "");
                    while (tempKey.endsWith(" ")) {
                        tempKey = tempKey.substring(0, tempKey.length() - 1);
                    }
                } else if (tempKey == null) {
                    throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
                }
                lines.remove(0);
                tempMap.put(tempKey, read(lines));
            } else {
                if (lines.get(0).contains(" = ")) {
                    String[] line = lines.get(0).split(" = ");
                    lines.remove(0);
                    tempMap.put(line[0], line[1]);
                } else {
                    if (lines.get(1).contains("{")) {
                        tempKey = lines.get(0);
                    } else {
                        throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
                    }
                }
            }
        }
        return tempMap;
        /*for (int i = id; i < lines.size(); i++) {
            while (lines.get(i).startsWith(" ")) {
                lines.set(i, lines.get(i).substring(1));
            }
            while (lines.get(i).endsWith(" ")) {
                lines.set(i, lines.get(i).substring(0, lines.get(i).length() - 1));
            }
            if (lines.get(i).endsWith("{")) {
                HashMap<String, Object> tempmap = new HashMap<>();
                String key;
                if (lines.get(i).equals("{")) {
                    key = lines.get(i - 1);
                } else {
                    key = lines.get(i).replace("{", "");
                }
                i = read(tempmap, lines, i);
                map.put(key, tempmap);
            } else if (lines.get(i).equals("}")) {
                return i;
            } else {
                String[] line = lines.get(i).split(" = ");
                map.put(line[0], line[1]);
            }
        }
        return lines.size() - 1;
         */
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