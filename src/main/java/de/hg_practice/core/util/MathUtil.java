package de.hg_practice.core.util;

import java.util.Random;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 22:32:42
 * ʕっ•ᴥ•ʔっ
 */

public class MathUtil {
    public static float divideAndCutoffF(int a, int b, int length) {
        return cutoff(a * 1f / b, length);
    }

    public static double divideAndCutoffD(int a, int b, int length) {
        return cutoff(a * 1d / b, length);
    }

    /**
     * Cuts off the last digits except for the first x = length ones.
     * @param f The float you want to cut digits off.
     * @param length The length of the number. For length = 2, 0.666 would be turned into 0.66
     * @return The new float with only the first x = length digits.
     */
    public static float cutoff(float f, int length) {
        float fac = (float) Math.pow(10, length);
        return ((int) (f * fac)) / fac;
    }

    public static double cutoff(double d, int length) {
        double fac = Math.pow(10, length);
        return ((int) (d * fac)) / fac;
    }

    public static boolean isInBetween(int i, int i1, int i2) {
        return (i1 <= i && i <= i2) || (i1 >= i && i >= i2);
    }

    public static boolean isInBetween(double i, double i1, double i2) {
        return (i1 <= i && i <= i2) || (i1 >= i && i >= i2);
    }

    /**
     * Returns a random number with binomial distribution.
     * <p>
     * <a href="https://en.wikipedia.org/wiki/Bernoulli_process">Bernoulli process on Wikipedia</a>
     * </p>
     * <p>
     * <a href= "https://en.wikipedia.org/wiki/Binomial_distribution">Binomial distribution on Wikipedia</a>
     * </p>
     * <p>
     *     <a href="https://shiny.rit.albany.edu/stat/binomial/">Visualization</a>
     * </p>
     *
     * @param random A random number generator
     * @param n      Amount of tries.
     * @param p      The probability of success on every try.
     * @return The amount of successes based on the bernoulli process.
     */
    public static int getRandomBinomial(Random random, int n, double p) {
        double log_q = Math.log(1.0 - p);
        int x = 0;
        double sum = 0;
        while (true) {
            sum += Math.log(random.nextDouble()) / (n - x);
            if (sum < log_q) {
                return x;
            }
            x++;
        }
    }

    /**
     * Returns a random integer (uniformly distributed) from the max to min (both inclusive).
     *
     * @param random A random number generator
     * @param min    The lower bound (inclusive)
     * @param max    The upper bound (inclusive)
     * @return A random integer in the given bound.
     */
    public static int getRandomUniform(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
