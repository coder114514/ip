package tobtahc;

class Utils {
    public static int nextRng(long cur, long inc) {
        var mul = ((22695477L * cur) & ((1L << 31) - 1));
        return (int)((mul + inc) & ((1L << 31) - 1));
    }
}
