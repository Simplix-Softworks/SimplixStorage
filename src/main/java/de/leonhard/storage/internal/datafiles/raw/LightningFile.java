package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.base.FileTypeUtils;
import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.exceptions.InvalidFileTypeException;
import de.leonhard.storage.internal.base.exceptions.InvalidSettingException;
import de.leonhard.storage.internal.base.exceptions.LightningFileReadException;
import de.leonhard.storage.internal.enums.FileType;
import de.leonhard.storage.internal.enums.ReloadSettings;
import de.leonhard.storage.internal.utils.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LightningFile extends FlatFile {
    private PrintWriter writer;

    public LightningFile(final File file, final InputStream inputStream, final ReloadSettings reloadSettings) throws InvalidFileTypeException {
        if (FileTypeUtils.isType(file, FileType.LIGHTNING)) {
            if (create(file)) {
                if (inputStream != null) {
                    FileUtils.writeToFile(this.file, inputStream);
                }
            }

            update();
            if (reloadSettings != null) {
                setReloadSettings(reloadSettings);
            }
        } else {
            throw new InvalidFileTypeException("The given file if of no valid filetype.");
        }
    }


    @Override
    protected void update() {
        try {
            this.fileData = new FileData(read());
        } catch (IOException | LightningFileReadException e) {
            System.err.println("Exception while reading LightningFile");
            e.printStackTrace();
        }
    }


    @Override
    public synchronized void remove(final String key) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        fileData.remove(finalKey);

        write(fileData.toMap());
    }


    @Override
    public synchronized void set(final String key, final Object value) {
        final String finalKey = (pathPrefix == null) ? key : pathPrefix + "." + key;

        synchronized (this) {

            String old = fileData.toString();
            fileData.insert(finalKey, value);

            if (!old.equals(fileData.toString())) {
                write(fileData.toMap());
            }
        }
    }

    private void write(Map<String, Object> map) {
        try {
            writer = new PrintWriter(file);
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
    Map<String, Object> read() throws LightningFileReadException, IOException {
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
                throw new LightningFileReadException("Block closed without being opened");
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
                    throw new LightningFileReadException("Key must not be null");
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
                        throw new LightningFileReadException("Key does not contain value or block");
                    }
                }
            }
        }
        return tempMap;
    }

    @SuppressWarnings("Duplicates")
    private Map<String, Object> read(List<String> lines) throws LightningFileReadException {
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
                throw new LightningFileReadException("Block closed without being opened");
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
                    throw new LightningFileReadException("Key must not be null");
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
                        throw new LightningFileReadException("Key does not contain value or block");
                    }
                }
            }
        }
        return tempMap;
    }

    @Override
    public Object get(final String key) {
        reload();
        String finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
        return fileData.get(finalKey);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            LightningFile lightningFile = (LightningFile) obj;
            return super.equals(lightningFile.getFlatFileInstance());
        }
    }
}