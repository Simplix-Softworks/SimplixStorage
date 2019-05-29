package de.leonhard.storage;

import java.io.File;

public interface LightningFile {



    /*
    Inheritance
     */

    static Yaml loadYaml(final File file) {
        return new Yaml(file);
    }

    static Json loadJson(final File file) {
        return new Json(file);
    }


}
