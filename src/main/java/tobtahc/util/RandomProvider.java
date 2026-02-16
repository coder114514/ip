package tobtahc.util;

/**
 * Provider for randomness.
 */
public interface RandomProvider {
    /**
     * Returns {@code true} with a probability of numerator/denominator.
     *
     * @param numerator the numerator of the probability fraction
     * @param denominator the denominator of the probability fraction
     * @return {@code true} with the specified probability; {@code false} otherwise
     */
    boolean chance(int numerator, int denominator);
}
