package tobtahc.util;

import java.util.Random;

/**
 * A provider for randomness using the standard Java RNG.
 */
public class Rng implements RandomProvider {
    private Random r;

    /**
     * Constructs a new {@code Rng} instance.
     */
    public Rng() {
        r = new Random();
    }

    @Override
    public boolean chance(int numerator, int denominator) {
        if (numerator < 0 || denominator <= 0 || numerator > denominator) {
            throw new IllegalArgumentException("illegal arguments passed to chance()");
        }
        return r.nextInt(denominator) < numerator;
    }
}
