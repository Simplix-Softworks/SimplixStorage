package de.leonhard.storage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Main {
    /*
    https://www.google.com/search?client=ubuntu&channel=fs&q=java+merge+two+files&ie=utf-8&oe=utf-8
    https://github.com/TheElectronWill/TOML-javalib

    Merge Files:
    https://stackoverflow.com/questions/14673063/merging-multiple-files-in-java

     */

    public static void main(String[] args) throws IOException, InterruptedException {


//        Yaml yaml = new Yaml("test", "");
//        yaml.setConfigSettings(ConfigSettings.preserveComments);
//        yaml.set("network", "k3");

        Config cfg = new Config("test", "");
        cfg.set("network", "k3");

        Thread.sleep(100);

        cfg.setHeader(Arrays.asList("#test", "#test", "#test"));


//        Toml toml = new Toml("aöalöa", "");
//        toml.setPathPrefix("linkskeinemitte");
//        final String rank = toml.getOrSetDefault("rank", "admin");
//        final String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date());
//        final String lastJoin = toml.getOrSetDefault("lastJoin", date);
//
//        toml.setPathPrefix("linkskeinemitte");
//        toml.set("test", "aka");


//        final File file = new File("test.yml");
//        final YamlEditor editor = new YamlEditor(file);
//        final List<String> lines = editor.read();
//        final YamlParser parser = new YamlParser(editor);
//        final List<String> updated = editor.readKeys();
//
//
//        parser.parseComments(updated).forEach(System.out::println);
//
//        editor.write(parser.parseComments(updated));


//        final ist<String> comments = editor.readPureComments();

//        editor.readPureComments().forEach(System.out::println);
    }


}
