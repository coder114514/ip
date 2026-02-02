package tobtahc.util;

/**
 * This class implements a few parser utilities.
 */
public class ParserUtil {
    /**
     * Find the index after consuming {@code tokenCount} tokens.
     *
     * @param s the string
     * @param tokenCount number of tokens to skip
     * @return the index
     */
    public static int indexAfterTokens(String s, int tokenCount) {
        int i = 0;
        int n = s.length();
        for (int t = 0; t < tokenCount && i < n; t++) {
            while (i < n && Character.isWhitespace(s.charAt(i))) i++;
            while (i < n && !Character.isWhitespace(s.charAt(i))) i++;
            while (i < n && Character.isWhitespace(s.charAt(i))) i++;
        }
        return i == n ? -1 : i;
    }
}
