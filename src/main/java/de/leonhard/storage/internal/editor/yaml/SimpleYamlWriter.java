package de.leonhard.storage.internal.editor.yaml;

import com.esotericsoftware.yamlbeans.YamlWriter;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.util.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Writer;

/**
 * Enhanced Version of YamlWriter of EsotericSoftware, which implements {@link AutoCloseable}
 */
public class SimpleYamlWriter extends YamlWriter implements AutoCloseable {

    public SimpleYamlWriter(final @NotNull Writer writer) {
        super(writer, LightningProviders.yamlConfig());
    }

    public SimpleYamlWriter(final @NotNull File file) {
        this(FileUtils.createWriter(file));
    }
}
