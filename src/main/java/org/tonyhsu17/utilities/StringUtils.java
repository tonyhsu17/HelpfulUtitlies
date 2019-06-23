package org.tonyhsu17.utilities;

public class StringUtils {
    private static final String INVALID_FILE_CHAR_REGEX = "[\\\\/:*?<>|]+"; // [\, /, :, *, ?, <, >, |]

    public static String santizeFileName(String text, String replaceStr) {
        return text.replaceAll(INVALID_FILE_CHAR_REGEX, replaceStr);
    }
}
