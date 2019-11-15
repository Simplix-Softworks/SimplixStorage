package de.leonhard.storage.internal.editor.yaml;

import de.leonhard.storage.utils.FileUtils;

import java.io.File;
import java.io.Reader;

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
}
