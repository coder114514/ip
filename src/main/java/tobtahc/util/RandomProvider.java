package tobtahc.util;

/**
 * The random provider used to provide some variety to chat bot's output.
 */
public interface RandomProvider {
    /**
     * Returns true with probability nominator/denominator, false otherwise.
     *
     * @param nominator the nominator
     * @param denominator the denominator
     * @return true with probability nominator/denominator, false otherwise
     */
    boolean chance(int nominator, int denominator);
}
