package de.leonhard.storage;


class Main {
    final static String DATADIR = "";


    public static void main(String[] args) {


        Yaml yaml = new Yaml("comments", "");
        yaml.setConfigSettings(ConfigSettings.preserveComments);
        yaml.set("a", 34);

    }

}
