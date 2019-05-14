package de.leonhard.storage;

public class Main {


    public static void main(String[] args) {
        Yaml yaml = new Yaml("test", "");
        yaml.set("test.test.test.test", "o3k3");
        yaml.set("playdata.fucked", true);

        Json json = new Json("test", "");

        json.set("gayerdata.gay.reallygay", "fck");
        System.out.println("Resultat: " + json.get("gayerdata.gay.reallygay"));


        yaml.set("gayerdata.gay.reallygay", false);

        System.out.println(json.contains("gayerdata.gay.reallygay"));


    }
}
