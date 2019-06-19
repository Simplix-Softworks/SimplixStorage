package de.leonhard.storage;

class Main {

    public static void main(String[] args) {
        final Yaml yaml = new Yaml("test", "");

        yaml.set("a", "b");
        yaml.setPathPrefix("test");
        yaml.set("c", "d");
        yaml.setPathPrefix(null);
        yaml.set("e", "f");

        final Json json = new Json("test", "");
        json.set("a", "b");
        json.setPathPrefix("test");
        json.set("c", "d");
        json.setPathPrefix(null);
        json.set("f", "g");

        json.getKeySet().forEach(System.out::println);
        System.out.println(" ");
        yaml.getKeySet().forEach(System.out::println);
    }

}
