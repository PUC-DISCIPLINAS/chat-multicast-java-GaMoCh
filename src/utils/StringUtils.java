package utils;

public final class StringUtils {
    private StringUtils() {
    }

    public static String leftTrim(String string, String substring) {
        return string.replaceFirst('^' + substring, "");
    }
}
