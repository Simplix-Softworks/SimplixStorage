package de.leonhard.storage;

import de.leonhard.storage.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Main {
    /*
    https://www.google.com/search?client=ubuntu&channel=fs&q=java+merge+two+files&ie=utf-8&oe=utf-8
    https://github.com/TheElectronWill/TOML-javalib

    Merge Files:
    https://stackoverflow.com/questions/14673063/merging-multiple-files-in-java

     */

    public static void main(String[] args) throws IOException {

        final File file = new File("test.yml");
        final YamlEditor editor = new YamlEditor(file);

        Yaml yaml = new Yaml("test", "");
        yaml.setConfigSettings(ConfigSettings.preserveComments);
        yaml.set("ich bin der geilste", "leonhard");


//        List<String> orginal = Arrays.asList("#eaaazeee");
//        List<String> updated = Arrays.asList("der Geilste: Leonhard");
//        List<String> merged = Utils.mergeLines(orginal, updated);


//        System.out.println("GEMERGED: " +  merged + " " + merged.size());
//
//        editor.write(merged);

//        reader.getHeader().forEach(System.out::println);


        ArrayList<String> a = new ArrayList<>(Arrays.asList("#ajjakajkaak", "#k2ok2l2", "working:false"));
        ArrayList<String> b = new ArrayList<>(Arrays.asList("working:true", "teamintern:true"));


//        System.out.println(Utils.mergeLines(a, b));

    }


}
