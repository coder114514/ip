package tobtahc.util;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility for low-level string parsing and token processing.
 */
public final class ParserUtil {
    /**
     * Finds the character index in a string after skipping a number of tokens.
     *
     * @param s the string to process
     * @param tokenCount the number of tokens to skip
     * @return the index in the string after the specified tokens
     */
    public static int findIndexAfterTokens(String s, int tokenCount) {
        int i = 0;
        int n = s.length();
        for (int t = 0; t < tokenCount && i < n; t++) {
            while (i < n && Character.isWhitespace(s.charAt(i))) {
                ++i;
            }
            while (i < n && !Character.isWhitespace(s.charAt(i))) {
                ++i;
            }
            while (i < n && Character.isWhitespace(s.charAt(i))) {
                ++i;
            }
        }
        return i;
    }

    /**
     * Parses the switches and descriptions from a command payload.
     * The returned map uses an empty string key for the main description.
     *
     * @param s the string payload to parse
     * @return a map of normalized switches and values, or {@code null} if
     *         parsing encounters unrecoverable syntax errors
     */
    public static Map<String, String> parseSwitches(String s) {
        var ret = new TreeMap<String, String>();
        if (s.length() == 0) {
            ret.put("", "");
            return ret;
        }

        if (s.endsWith("/")) {
            return null;
        }

        int i = findNextSlash(s, 0);
        ret.put("", s.substring(0, i++).trim());

        while (i < s.length()) {
            if (!Character.isAlphabetic(s.charAt(i))) {
                return null;
            }
            int j = i;
            while (j < s.length() && Character.isAlphabetic(s.charAt(j))) {
                ++j;
            }
            if (j < s.length() && !Character.isWhitespace(s.charAt(j))) {
                return null;
            }
            var key = s.substring(i, j).toLowerCase(Locale.ROOT);
            if (ret.containsKey(key)) {
                return null;
            }
            int k = findNextSlash(s, j);
            var value = j < s.length() ? s.substring(j, k).trim() : "";
            ret.put(key, value);
            i = k + 1;
        }

        return ret;
    }

    private static int findNextSlash(String s, int i) {
        for (; i < s.length(); ++i) {
            if (s.charAt(i) == '/') {
                return i;
            }
        }
        return i;
    }
}
