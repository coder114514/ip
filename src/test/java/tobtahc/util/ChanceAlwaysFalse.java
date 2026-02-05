package tobtahc.util;

/**
 * Mock RNG that always returns false.
 */
public class ChanceAlwaysFalse implements RandomProvider {
    @Override
    public boolean chance(int nominator, int denominator) {
        return false;
    }
}
