package de.leonhard.storage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

class Main {
    /*
    https://www.google.com/search?client=ubuntu&channel=fs&q=java+merge+two+files&ie=utf-8&oe=utf-8
    https://github.com/TheElectronWill/TOML-javalib

    Merge Files:
    https://stackoverflow.com/questions/14673063/merging-multiple-files-in-java

     */

    public static void main(String[] args) throws IOException {

//
//        Yaml yaml = new Yaml("test", "");
//        yaml.setConfigSettings(ConfigSettings.preserveComments);
//        yaml.set("network", "k3");
        final File file = new File("test.yml");
        final YamlEditor editor = new YamlEditor(file);
        final List<String> lines = editor.read();
        final YamlParser parser = new YamlParser(editor);
        final List<String> updated = editor.readKeys();


        parser.getLinesWithComments(updated).forEach(System.out::println);

//        lines.forEach(System.out::println);


//        final ist<String> comments = editor.readPureComments();

//        editor.readPureComments().forEach(System.out::println);
    }


}
