package gf2t;

import java.util.Optional;
import java.util.Random;
import org.junit.*;

public class GF2_192Benchmark {

    @Test
    public void time() {

        GF2_192 p;
        long[] d = new long[3];
        GF2_192 res = new GF2_192();
        Random rand = new Random();
        d[0] = rand.nextLong();
        d[1] = rand.nextLong();
        d[2] = rand.nextLong();
        p = new GF2_192(d);
        long t1, t2, t3, t4;

        int numSqr = 100000000;
        t1 = System.nanoTime();
        for (int i = 0; i<numSqr; i++) {
            GF2_192.sqr(res, p);
        }
        t2 = System.nanoTime();
        System.out.printf("Squaring        %4.1f ns\n", (t2-t1)/(double)numSqr);

        int numMult = 20000000;
        t3 = System.nanoTime();
        for (int i = 0; i<numMult; i++) {
            GF2_192.mul(res, p,p);
        }
        t4 = System.nanoTime();
        System.out.printf("Multiplication  %5.1f ns",(t4-t3)/(double)numMult);

        System.out.printf(" (squaring is %2.1f times faster)\n",((double)t4-t3)/(t2-t1)*numSqr/(double)numMult);


        int numpow2To64 = 20000000;
        t1 = System.nanoTime();
        for (int i = 0; i<numpow2To64; i++) {
            GF2_192.power2To2ToK(res, p, 6);
        }
        t2 = System.nanoTime();
        System.out.printf("Power 2^64      %5.1f ns\n", (t2-t1)/(double) numpow2To64);

        int numInv = 1000000;
        t1 = System.nanoTime();
        for (int i = 0; i < numInv; i++) {
            d[0] = rand.nextLong();
            d[1] = rand.nextLong();
            d[2] = rand.nextLong();
            p = new GF2_192(d);
            GF2_192.invert(res, p);
        }
        t2 = System.nanoTime();

        System.out.printf("Inversion      %5.1f ns\n", (t2 - t1) / (double)numInv);

        int [] interpolateLengths = {5, 10, 20};
        int [] numInterpolate = {200000, 50000, 30000};
        GF2_192[] values10 = null;
        byte[] points10 = null;
        GF2_192 valueAt0 = null;

        for (int k = 0; k< interpolateLengths.length; k++) {
            int len = interpolateLengths[k];
            GF2_192[] values = new GF2_192[len - 1];
            byte[] points = new byte[len-1];
            for (byte i = 0; i<len-1; i++) {
                points[i]=(byte)(i+1);
            }

            byte[] temp = new byte[24];

            for (int j = 0; j < len - 1; j++) {
                rand.nextBytes(temp);
                values[j] = new GF2_192(temp);
            }
            if (len==10) {
                // Save these for the eval test
                points10 = points;
                values10 = values;
                valueAt0 = new GF2_192(temp);
            }

            rand.nextBytes(temp);

            GF2_192 opt0 = new GF2_192(temp);
            t1 = System.nanoTime();
            for (int i = 0; i < numInterpolate[k]; i++) {
                GF2_192_Poly.interpolate(points, values, opt0);
            }
            t2 = System.nanoTime();
            System.out.printf("Interpolation of %2d points %5.1f ns\n", len, (t2 - t1) / (double) numInterpolate[k]);
        }


        GF2_192_Poly poly10 =  GF2_192_Poly.interpolate(points10, values10, valueAt0);

        int numEval = 400000;
        byte[] points = new byte[20];
        for (byte i = 0; i<points.length; i++) {
            points[i]=(byte)(i+1);
        }
        t1 = System.nanoTime();
        for (int i = 0; i < numEval; i++) {
            for (byte point:points) {
                poly10.evaluate(point);
            }
        }
        t2 = System.nanoTime();
        System.out.printf("Evaluation of degree-9 on 20 points %5.1f ns\n", (t2 - t1) / (double) numEval);
        System.out.println("Polynomial: "+ poly10);

    }







}