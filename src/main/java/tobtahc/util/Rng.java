package tobtahc.util;

import java.util.Random;

/**
 * The RNG class.
 */
public class Rng implements RandomProvider {
    private Random r;

    /**
     * Initializes the RNG.
     */
    public Rng() {
        r = new Random();
    }

    @Override
    public boolean chance(int nominator, int denominator) {
        return r.nextInt(denominator) < nominator;
    }
}
