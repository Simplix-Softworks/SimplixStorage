package de.leonhard.storage;

public class Main {

    public static void main(String[] args) {

        Yaml yaml = new Yaml("test", "");

        String [] test = {"a", "b", "c", "d", "e"};
        yaml.set("test", test);

        yaml.getStringList("test").toArray();

        System.out.println(yaml.getStringList("test"));

    }

}
