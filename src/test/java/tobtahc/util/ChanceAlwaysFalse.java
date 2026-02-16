package tobtahc.util;

/**
 * Mock RNG that always returns {@code false}.
 */
public class ChanceAlwaysFalse implements RandomProvider {
    @Override
    public boolean chance(int numerator, int denominator) {
        return false;
    }
}
