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
        if (nominator < 0 || denominator <= 0 || nominator > denominator) {
            throw new IllegalArgumentException("illegal arguments passed to chance()");
        }
        return r.nextInt(denominator) < nominator;
    }
}
