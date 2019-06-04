package de.leonhard.storage.base;

import java.util.List;

public interface ConfigBase extends YamlBase {


    List<String> getHeader();

    void setHeader(final List<String> header);

}
