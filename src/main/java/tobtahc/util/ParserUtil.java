package tobtahc.util;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class implements a few parser utilities.
 */
public final class ParserUtil {
    /**
     * Finds the index after consuming {@code tokenCount} tokens.
     *
     * @param s the string
     * @param tokenCount number of tokens to skip
     * @return the index
     */
    public static int indexAfterTokens(String s, int tokenCount) {
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
     * Parses the switches in the payload part of a task command.
     * And in the returned map, "" corresponds to the description part.
     * And it will normalize the keys.
     *
     * @param s the string payload
     * @return the map containing switches and their values, null if there
     *     are unrecoverable errors like a slash followed by space(s),
     *     duplicate keys, keys with non-alphabetic characters etc
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

        int i = nextSlash(s, 0);
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
            int k = nextSlash(s, j);
            var value = j < s.length() ? s.substring(j, k).trim() : "";
            ret.put(key, value);
            i = k + 1;
        }

        return ret;
    }

    private static int nextSlash(String s, int i) {
        for (; i < s.length(); ++i) {
            if (s.charAt(i) == '/') {
                return i;
            }
        }
        return i;
    }
}
