package de.hg_practice.core.util;

public class Elo {

    /**
     * A quick function to calculate Elo ratings. Follows algorithm found
     * <a href="https://en.wikipedia.org/wiki/Elo_rating_system#Mathematical_details">on Wikipedia</a>.
     *
     * This version returns a primitive int array with the new ratings, but you
     * can modify it as needed. The important variables to watch out for are "an"
     * and "bn", which hold the new ratings for player A and player B respectively.
     *
     * @param a
     *   The rating for player A.
     * @param b
     *   The rating for player B.
     * @param aW
     *   Boolean whether A is the winner or not.
     *   True: if a is the winner.
     *   False: if b is the winner.
     * @param k
     *   The k-factor to use for the calculation. Higher values will make ratings
     *   change more drastically.
     * @return
     *   An int[] primitive array, with the first int being A's new rating, and the
     *   second being B's new ranking.
     */
    public static int[] calculateNewRatings(int a, int b, boolean aW, int k) {
        double ea = 1 / (1 + Math.pow(10, (b - a) / 400d));
        double eb = 1 - ea;

        int sa = aW ? 1 : 0;
        int sb = 1 - sa;

        int an = (int) Math.floor(a + k * (sa - ea));
        int bn = (int) Math.floor(b + k * (sb - eb));

        return new int[]{an, bn};
    }
}
