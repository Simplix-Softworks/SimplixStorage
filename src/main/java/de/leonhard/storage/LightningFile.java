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

@SuppressWarnings({"unused", "WeakerAccess"})
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


    @Override
    protected void update() {
        try {
            this.fileData = new FileData(read());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    if (localKey.startsWith("#") && map.get(localKey) == null) {
                        writer.println(localKey);
                    } else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
                        writer.println("");
                    } else if (map.get(localKey) instanceof Map) {
                        writer.println(localKey + " " + "{");
                        //noinspection unchecked
                        write((Map<String, Object>) map.get(localKey), "");
                    } else {
                        writer.println(localKey + " = " + map.get(localKey));
                    }
                }
                writer.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(Map<String, Object> map, String indentationString) {
        for (String localKey : map.keySet()) {
            if (localKey.startsWith("#") && map.get(localKey) == null) {
                writer.println(indentationString + "  " + localKey);
            } else if (localKey.startsWith("{=}emptyline") && map.get(localKey) == null) {
                writer.println("");
            } else if (map.get(localKey) instanceof Map) {
                writer.println(indentationString + "  " + localKey + " " + "{");
                //noinspection unchecked
                write((Map<String, Object>) map.get(localKey), indentationString + "  ");
            } else {
                writer.println(indentationString + "  " + localKey + " = " + map.get(localKey));
            }
        }
        writer.println(indentationString + "}");
    }

    @SuppressWarnings("Duplicates")
    Map<String, Object> read() throws IOException {
        List<String> lines = FileUtils.readAllLines(this.file);
        Map<String, Object> tempMap = new HashMap<>();
        String tempKey = null;
        while (lines.size() > 0) {
            lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            while (lines.get(0).startsWith(" ")) {
                lines.set(0, lines.get(0).substring(1));
            }
            while (lines.get(0).endsWith(" ")) {
                lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            }

            if (lines.get(0).contains("}")) {
                throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
            } else if (lines.get(0).isEmpty()) {
                tempMap.put("{=}emptyline" + lines.size(), null);
                lines.remove(0);
            } else if (lines.get(0).startsWith("#")) {
                tempMap.put(lines.get(0), null);
                lines.remove(0);
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
                        lines.remove(0);
                    } else {
                        throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
                    }
                }
            }
        }
        return tempMap;
    }

    @SuppressWarnings("Duplicates")
    private Map<String, Object> read(List<String> lines) throws InvalidObjectException {
        Map<String, Object> tempMap = new HashMap<>();
        String tempKey = null;
        while (lines.size() > 0) {
            lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            while (lines.get(0).startsWith(" ")) {
                lines.set(0, lines.get(0).substring(1));
            }
            while (lines.get(0).endsWith(" ")) {
                lines.set(0, lines.get(0).substring(0, lines.get(0).length() - 1));
            }

            if (lines.get(0).equals("}")) {
                lines.remove(0);
                return tempMap;
            } else if (lines.get(0).contains("}")) {
                throw new InvalidObjectException("I MUST GEN NEW EXCEPTION");
            } else if (lines.get(0).isEmpty()) {
                tempMap.put("{=}emptyline", null);
                lines.remove(0);
            } else if (lines.get(0).startsWith("#")) {
                tempMap.put(lines.get(0), null);
                lines.remove(0);
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