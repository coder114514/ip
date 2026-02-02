package tobtahc.util;

/**
 * My custom simple RNG.
 */
public class Rng {
    private int rng;

    /**
     * Initializes the RNG with 0.
     */
    public Rng() {
        rng = 0;
    }

    /**
     * Updates the RNG with {@code inc}.
     *
     * @param inc the incremental value
     * @return updated RNG
     */
    public int nextRng(int inc) {
        var mul = ((22695477L * rng) & ((1L << 31) - 1));
        rng = (int)((mul + inc) & ((1L << 31) - 1));
        return rng;
    }

    /**
     * {@return the current RNG}
     */
    public int getRng() {
        return rng;
    }
}
