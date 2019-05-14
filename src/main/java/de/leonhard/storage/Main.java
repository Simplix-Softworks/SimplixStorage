package de.leonhard.storage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
        Yaml yaml = new Yaml("test", "");
        Json json = new Json("test", "");


        ArrayList test = new ArrayList(Arrays.asList("test", "test", "test", "Test"));


//
//
//        json.set("linkskeinemitte.rank", "admin");
//        json.set("linkskeinemitte.name", "linkskeinemitte");
//        json.set("linkskeinemitte.lastjoin", "jetzt");
//        json.set("linkskeinemitte.join.lastjoin", "jetzt");
//        json.set("linskeinemitte.join.firstjoin", "jetzt");


        json.set("test.test.test.test", true);

        yaml.set("a.b.c", test);

//        yaml.set("linkskeinemitte.rank", "admin");
//        yaml.set("linkskeinemitte.name", "linkskeinemitte");
//        yaml.set("linkskeinemitte.lastjoin", "jetzt");
//        yaml.set("test.test", "a");
    }
}
