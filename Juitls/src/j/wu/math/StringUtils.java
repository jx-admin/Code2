package com.hola.launcher.util;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtils {

    public static final char BLANK = '　';

    public static String join(Object[] collections, String seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Object obj : collections) {
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }

            String text = obj == null ? "" : obj.toString();

            builder.append(text);
        }

        return builder.toString();
    }

    public static String join(Collection<? extends Object> collections, String seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Iterator<? extends Object> iter = collections.iterator(); iter.hasNext();) {
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }

            Object obj = iter.next();
            String text = obj == null ? "" : obj.toString();

            builder.append(text);
        }

        return builder.toString();
    }

    public static String join(Collection<? extends Object> collections, char seperator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Iterator<? extends Object> iter = collections.iterator(); iter.hasNext();) {
            if (!first) {
                builder.append(seperator);
            } else {
                first = false;
            }

            Object obj = iter.next();

            append(builder, obj, seperator);
        }

        return builder.toString();
    }

    private static void append(StringBuilder builder, Object obj, char seperator) {
        String text = Uri.encode(obj == null ? "" : obj.toString());

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == seperator) {
                builder.append('\\');
            } else if (text.charAt(i) == '\\') {
                builder.append('\\');
            }
            builder.append(text.charAt(i));
        }
    }

    public static String[] split(String value, char seperator) {
        if (value.length() == 0) {
            return new String[0];
        }

        value += seperator;

        List<String> ret = new ArrayList<String>();

        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '\\') {
                // we may get the escape character here
                if (i + 1 < value.length()) {
                    i++;
                }
            }

            if (value.charAt(i) == seperator) {
                ret.add(Uri.decode(temp.toString()));
                temp.delete(0, temp.length());
            } else {
                temp.append(value.charAt(i));
            }
        }

        return ret.toArray(new String[ret.size()]);
    }

    public static String trimString(String str) {
        if (str == null) {
            return null;
        }

        int start;
        int end;
        for (start = 0; start < str.length(); start++) {
            if (!Character.isWhitespace(str.charAt(start))
                    && !Character.isSpaceChar(str.charAt(start))
                    && str.charAt(start) != StringUtils.BLANK) {
                break;
            }
        }
        if (start == str.length()) {
            return "";
        }
        for (end = str.length() - 1; end >= 0; end--) {
            if (!Character.isWhitespace(str.charAt(end))
                    && !Character.isSpaceChar(str.charAt(end))
                    && str.charAt(end) != StringUtils.BLANK) {
                break;
            }
        }
        return str.substring(start, end + 1);
    }


    /**
     * Checks if a String is empty ("") or null.
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Checks if a String is whitespace, empty ("") or null.
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * <p>
     * Convert a <code>String</code> to an <code>int</code>, returning a default
     * value if the conversion fails.
     * </p>
     * <p/>
     * <p>
     * If the string is <code>null</code>, the default value is returned.
     * </p>
     * <p/>
     * <pre>
     *   NumberUtils.toInt(null, 1) = 1
     *   NumberUtils.toInt("", 1)   = 1
     *   NumberUtils.toInt("1", 0)  = 1
     * </pre>
     *
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion
     * fails
     */
    public static int parseInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static char getChar(int i) {
        return (char) ('a' + i);
    }
}
