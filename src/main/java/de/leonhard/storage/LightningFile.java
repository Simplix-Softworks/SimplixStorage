package de.leonhard.storage;

import java.io.File;

public interface LightningFile {


    /*
    Make files more readable

    ->
    Base class:
    YamlFile -> private only basic methods
    ->Advanceda in new classes
    ->
     */



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
