package de.leonhard.storage.base;

import org.json.JSONObject;

import java.io.IOException;

public interface JsonBase extends StorageBase {

    void write(final JSONObject object) throws IOException;

}
