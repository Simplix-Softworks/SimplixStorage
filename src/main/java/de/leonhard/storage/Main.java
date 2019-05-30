package de.leonhard.storage;

public class Main {

    public static void main(String[] args) {
        Yaml yaml = new Yaml("test", "");
        yaml.set("working", true);
        yaml.set("ich bin der geilste" , "leonhard");
        yaml.set("k22", 33);
        yaml.set("3ölö3", ',');
        yaml.set("k22", 33);
    }

}
