package de.leonhard.storage;

class Main {

    public static void main(String[] args) {
      final Yaml yaml = new Yaml("test", "");
      yaml.setConfigSettings(ConfigSettings.preserveComments);

      yaml.set("working", "true");


      final Toml toml = new Toml("test", "");
      toml.set("y", 3.4);
      System.out.println(toml.getFloat("y"));
    }
}






