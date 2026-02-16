package tobtahc.util;

/**
 * Mock RNG that always returns {@code true}.
 */
public class ChanceAlwaysTrue implements RandomProvider {
    @Override
    public boolean chance(int numerator, int denominator) {
        return true;
    }
}
