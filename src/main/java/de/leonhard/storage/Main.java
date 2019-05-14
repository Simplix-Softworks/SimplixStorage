package de.leonhard.storage;

public class Main {


    public static void main(String[] args) {
        Yaml yaml = new Yaml("test", "");



        System.out.println(yaml.getString("test.test.test.test"));
    }


    public static void stackOvaflow(int a) {
        stackOvaflow(a + 1);
    }
}
