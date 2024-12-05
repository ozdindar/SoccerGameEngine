package util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by dindar.oz on 9/29/2017.
 */
public class RandomUtils {
    static Random rng = new SecureRandom();

    public static double randomBinomial()
    {
        return  -1 + rng.nextDouble()*2; //rng.nextDouble()-rng.nextDouble();
    }

    public static int nextInt(int size) {
        return rng.nextInt(size);
    }


    public static double nextDouble() {
        return rng.nextDouble();
    }

    public static boolean nextBoolean(double p) {
        return (nextDouble()<=p);
    }
}
