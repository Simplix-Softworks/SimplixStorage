package de.leonhard.storage;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Yaml yaml = new Yaml("test", "");
        Json json = new Json("test", "");

        Scanner scanner = new Scanner(System.in);

        System.out.println("-----BENCHMARKER-----");
        System.out.println(" ");
        System.out.println("Type: 'true' to run the benchmark");

        if(scanner.nextBoolean()){
            System.out.println("");
        }

        final long ms = System.currentTimeMillis();

        System.out.println("");

        System.out.println(yaml.get("linkskeinemitte"));

        final long timeNeeded = System.currentTimeMillis() - ms;

        System.out.println("  ");

        System.out.println("Benchmark took " + timeNeeded + " ms");


//        yaml.setPathPrefix("linkskeinemitte.a.b");
//
//        yaml.set("j3j3", "k2ok3o3");
//        yaml.set("k3k3l", "33l3kl<ä");


    }


}



