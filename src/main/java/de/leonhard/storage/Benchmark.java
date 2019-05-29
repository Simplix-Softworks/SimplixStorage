package de.leonhard.storage;

import java.util.ArrayList;
import java.util.Scanner;

public class Benchmark {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("-----BENCHMARKER-----");
        System.out.println("How many times should the benchmark run?");

        final byte runs = scanner.nextByte();
        long result = 0;
                /*
        Results:

          Instancing&Parsing.:
            YAML: 1) 51 2) 52
            JSON: 1) 5 2) 21

         */

                /*
                Instanzing:

                Setting:

                Getting:

                Getting Nested:
                 */

        int i = 0;
        final long start = System.currentTimeMillis();
//        test.set("a.b.c.d.e.f.g.h.i.k.l.m.n.o.p.q.r.t.u.v.w.x.y.z", true);
        while (i < runs) {
            final long ms = System.currentTimeMillis();
            //

            Json test = new Json("bench", "");
            test.get("a.b.c.d.e.f.g.h.i.k.l.m.n.o.p.q.r.t.u.v.w.x.y.z");

            //
            final long timeNeeded = System.currentTimeMillis() - ms;

            System.out.println("RUN " + i + " needed " + timeNeeded + " ms.");
            result += timeNeeded;
            i++;
        }

        System.out.println("Loops: " + runs);
        System.out.println("Average per loop: " + ((System.currentTimeMillis() - start) / (double) runs) + " ms.");
        System.out.println("Time: " + (System.currentTimeMillis() - start) + " ms");


        System.out.println("  ");


//        yaml.setPathPrefix("linkskeinemitte.a.b");
//
//        yaml.set("j3j3", "k2ok3o3");
//        yaml.set("k3k3l", "33l3kl<ä");


    }


}



