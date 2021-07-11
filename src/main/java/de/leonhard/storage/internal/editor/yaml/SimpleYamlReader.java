package de.leonhard.storage.internal.editor.yaml;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import de.leonhard.storage.util.FileUtils;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Version of YamlReader of EsotericSoftware, which implements {@link AutoCloseable}
 */
@SuppressWarnings("unused")
public class SimpleYamlReader
        extends YamlReader
        implements AutoCloseable {

    public SimpleYamlReader(final @NotNull Reader reader) {
        super(reader);
    }

    public SimpleYamlReader(final @NotNull File file) {
        super(FileUtils.createReader(file));
    }

    public SimpleYamlReader(final @NotNull String yaml) {
        super(yaml);
    }

    @SuppressWarnings("unchecked")
    public @NotNull Map<String, Object> readToMap() throws YamlException {
        val obj = read();
        return obj == null ? new HashMap<>() : (Map<String, Object>) obj;
    }
}
