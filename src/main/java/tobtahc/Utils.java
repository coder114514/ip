package tobtahc;

/**
 * This class contains some utilities.
 */
class Utils {
    /**
     * Update the RNG with inc.
     *
     * @param cur Current RNG.
     * @param inc The incremental value.
     * @return New RNG.
     */
    public static int nextRng(long cur, long inc) {
        var mul = ((22695477L * cur) & ((1L << 31) - 1));
        return (int)((mul + inc) & ((1L << 31) - 1));
    }
}
