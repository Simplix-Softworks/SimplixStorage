package de.leonhard.storage.internal.editor.yaml;

import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.util.FileUtils;

import java.io.File;
import java.io.Writer;

/** Enhanced Version of YamlWriter of EsotericSoftware which implements AutoClosable */
public class YamlWriter extends com.esotericsoftware.yamlbeans.YamlWriter implements AutoCloseable {

  public YamlWriter(final Writer writer) {
    super(writer, LightningProviders.yamlConfig());
  }

  public YamlWriter(final File file) {
    this(FileUtils.createWriter(file));
  }
}
