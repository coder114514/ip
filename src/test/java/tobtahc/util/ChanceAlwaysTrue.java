package tobtahc.util;

/**
 * Mock RNG that always returns true.
 */
public class ChanceAlwaysTrue implements RandomProvider {
    @Override
    public boolean chance(int nominator, int denominator) {
        return true;
    }
}
