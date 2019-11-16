package de.leonhard.storage.internal.editor.yaml;

import com.esotericsoftware.yamlbeans.YamlException;
import de.leonhard.storage.utils.FileUtils;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Version of YamlReader of EsotericSoftware which
 * implements AutoClosable
 */
public class YamlReader extends com.esotericsoftware.yamlbeans.YamlReader implements AutoCloseable {
    public YamlReader(Reader reader) {
        super(reader);
    }

    public YamlReader(File file) {
        super(FileUtils.createReader(file));
    }

    public YamlReader(String yaml) {
        super(yaml);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readToMap() throws YamlException {
        final Object obj = read();
        return obj == null ? new HashMap<>() : (Map<String, Object>) obj;
    }
}
