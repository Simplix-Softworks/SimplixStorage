package de.leonhard.storage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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


//        Yaml yaml = new Yaml("test", "");
//        yaml.setConfigSettings(ConfigSettings.preserveComments);
//        yaml.set("network", "k3");


        Config cfg = new Config("test", "");
        cfg.set("network", "hjh");
//        cfg.getHeader().forEach(System.out::println);
        cfg.setHeader(Arrays.asList("#ejkjekejkejkjekej", "#aklkalkalkalkalkalkal", "#akalkalkalkakalkalaklaklaklaklak"));

//        final File file = new File("test.yml");
//        final YamlEditor editor = new YamlEditor(file);
//        final List<String> lines = editor.read();
//        final List<String> comments = editor.getPureComments();
//        lines.removeAll(comments);

//        editor.getPureComments().forEach(System.out::println);

//        YamlParser parser = new YamlParser(editor);

//        final Map<String, List<String>> parsed = parser.assignCommentsToKey();


//        for(final String key : parsed.keySet()){
//            System.out.println(lines.contains(key));
//        }




//        for (final String key : parsed.keySet()) {
//            int i = 0;
//            for (final String line : parsed.get(key)) {
//                if (line.isEmpty())
//                    continue;
////                System.out.println("LINE" + key);
//                if (lines.contains(key))
//                    lines.add(lines.indexOf(key) + i, line);
//            }
//        }



//        lines.forEach(System.out::println);
//
//        for(final String key : parser.assignCommentsToKey().keySet()){
//
//        }


//        final List<String> lines = editor.read();


//        YamlEditor.getFooterFromLines(lines).forEach(System.out::println);
//
//        Yaml yaml = new Yaml("test", "");
//        yaml.setConfigSettings(ConfigSettings.preserveComments);
//        yaml.set("network", "kl");


    }


}
