package de.leonhard.storage;

import java.io.*;

 class Main {
    /*
    https://www.google.com/search?client=ubuntu&channel=fs&q=java+merge+two+files&ie=utf-8&oe=utf-8
    https://github.com/TheElectronWill/TOML-javalib

    Merge Files:
    https://stackoverflow.com/questions/14673063/merging-multiple-files-in-java

     */

    public static void main(String[] args) throws IOException {

        final File file = new File("test.yml");

        YamlReader reader = new YamlReader(file);

//        reader.read().forEach(System.out::println);

        reader.getHeader().forEach(System.out::println);
    }

}
