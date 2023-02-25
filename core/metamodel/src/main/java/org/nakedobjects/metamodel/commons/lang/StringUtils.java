package org.nakedobjects.metamodel.commons.lang;

import java.util.List;


public final class StringUtils {
    private StringUtils() {}

    public static String naturalName(final String name) {

        int pos = 0;

        // find first upper case character
        while ((pos < name.length()) && Character.isLowerCase(name.charAt(pos))) {
            pos++;
        }

        if (pos == name.length()) {
            return "invalid name";
        }
        return naturalize(name, pos);
    }

    public static String naturalize(final String name) {
        return naturalize(name, 0);
    }

    private static String naturalize(final String name, final int startingPosition) {
        if (name.length() <= startingPosition) {
            throw new IllegalArgumentException("string shorter than starting position provided");
        }
        final StringBuffer s = new StringBuffer(name.length() - startingPosition);
        for (int j = startingPosition; j < name.length(); j++) { // process english name - add spaces
            if ((j > startingPosition) && isStartOfNewWord(name.charAt(j), name.charAt(j - 1))) {
                s.append(' ');
            }
            if (j == startingPosition) {
                s.append(Character.toUpperCase(name.charAt(j)));
            } else {
                s.append(name.charAt(j));
            }
        }
        final String str = s.toString();
        return str;
    }

    private static boolean isStartOfNewWord(final char c, final char previousChar) {
        return Character.isUpperCase(c) || Character.isDigit(c) && !Character.isDigit(previousChar);
    }

    public static String capitalize(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

    public static String lowerFirst(final String argsPhrase) {
        if (argsPhrase == null || argsPhrase.length() == 0) {
            return argsPhrase;
        }
        if (argsPhrase.length() == 1) {
            return argsPhrase.toLowerCase();
        }
        return argsPhrase.substring(0, 1).toLowerCase() + argsPhrase.substring(1);
    }

    public static boolean in(final String str, final String[] strings) {
        for (final String strCandidate : strings) {
            if (strCandidate.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static String commaSeparatedClassNames(final List<Object> objects) {
        final StringBuilder buf = new StringBuilder();
        final int i = 0;
        for (final Object object : objects) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append(object.getClass().getName());
        }
        return buf.toString();
    }

    public static String stripNewLines(final String str) {
        return str.replaceAll("[\r\n]", "");
    }

    public static String combine(List<String> list) {
        final StringBuffer buf = new StringBuffer();
        for (String message: list) {
            if (list.size() > 1) {
                buf.append("; ");
            }
            buf.append(message);
        }
        return buf.toString();
    }

    public static String firstWord(String line) {
        String[] split = line.split(" ");
        return split[0];
    }

	public static String stripLeadingSlash(String path) {
	    if (!path.startsWith("/")) {
	        return path;
	    }
	    if (path.length() < 2) {
	        return "";
	    }
	    return path.substring(1);
	}

	public static String removeTabs(final String text) {
	    // quick return - jvm java should always return here
	    if (text.indexOf('\t') == -1) {
	        return text;
	    }
	    final StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < text.length(); i++) {
	        // a bit clunky to stay with j# api
	        if (text.charAt(i) != '\t') {
	            buf.append(text.charAt(i));
	        }
	    }
	    return buf.toString();
	}

}
