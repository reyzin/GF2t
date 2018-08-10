package gf2t;

import java.util.Random;
import org.junit.*;

public class GF2_128Benchmark {

    @Test
    public void time() {
        GF2_128 p;
        long[] d = new long[2];
        GF2_128 res = new GF2_128();
        Random rand = new Random();
        d[0] = rand.nextLong();
        d[1] = rand.nextLong();
        p = new GF2_128(d);
        long t1, t2, t3, t4;



        int numSqr = 1000000000;
        t1 = System.nanoTime();
        for (int i = 0; i<numSqr; i++) {
            GF2_128.sqr(res, p);
        }
        t2 = System.nanoTime();
        System.out.printf("Squaring        %4.1f ns\n", (t2-t1)/(double)numSqr);

        int numMult = 100000000;
        t3 = System.nanoTime();
        for (int i = 0; i<numMult; i++) {
            GF2_128.mul(res, p,p);
        }
        t4 = System.nanoTime();
        System.out.printf("Multiplication %4.1f ns",(t4-t3)/(double)numMult);

        System.out.printf(" (squaring is %2.1f times faster)\n",((double)t4-t3)/(t2-t1)*numSqr/(double)numMult);


        int numpow65536 = 500000000;
        t1 = System.nanoTime();
        for (int i = 0; i<numpow65536; i++) {
            GF2_128.pow65536(res, p);
        }
        t2 = System.nanoTime();
        System.out.printf("Power 65536     %4.1f ns\n", (t2-t1)/(double) numpow65536);

        int numInv = 1000000;
        t1 = System.nanoTime();
        for (int i = 0; i < numInv; i++) {
            d[0] = rand.nextLong();
            d[1] = rand.nextLong();
            p = new GF2_128(d);
            GF2_128.invert(res, p);
        }
        t2 = System.nanoTime();

        System.out.printf("Inversion     %4.1f ns\n", (t2 - t1) / (double)numInv);


    }
}
