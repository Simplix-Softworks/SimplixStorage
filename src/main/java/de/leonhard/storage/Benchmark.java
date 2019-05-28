package de.leonhard.storage;

import java.util.Scanner;

public class Benchmark {

    public static void main(String[] args) {
        final Yaml yaml = new Yaml("test", "");
        final Json json = new Json("test", "");

        Scanner scanner = new Scanner(System.in);

        System.out.println("-----BENCHMARKER-----");
//        System.out.println(" ");
        System.out.println("Type: 'true' to run the benchmark");

        if (scanner.nextBoolean()) {
        }
                /*
        Results:

          Instancing&Parsing.:
            YAML: 1) 51 2) 52
            JSON: 1) 5 2) 21

         */

        final long ms = System.currentTimeMillis();

        Json test = new Json("bench", "");

        test.get("a");


//        test.set("a.b.c.d.e.f.g.h.i.k.l.l.m.n.o.p.q.r", true);

        final long timeNeeded = System.currentTimeMillis() - ms;

        System.out.println("  ");

        System.out.println("Benchmark took " + timeNeeded + " ms");


//        yaml.setPathPrefix("linkskeinemitte.a.b");
//
//        yaml.set("j3j3", "k2ok3o3");
//        yaml.set("k3k3l", "33l3kl<ä");


    }


}



