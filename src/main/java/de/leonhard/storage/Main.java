package de.leonhard.storage;

public class Main {
    public static void main(String[] args) {
        final Yaml yaml = new Yaml("test", "");
        yaml.getString("MySQL.User");
    }
}
