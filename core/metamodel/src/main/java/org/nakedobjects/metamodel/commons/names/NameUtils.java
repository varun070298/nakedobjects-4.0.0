package org.nakedobjects.metamodel.commons.names;

public final class NameUtils {

    private NameUtils() {}

    /**
     * Returns the name of a Java entity without any prefix. A prefix is defined as the first set of lowercase
     * letters and the name is characters from, and including, the first upper case letter. If no upper case
     * letter is found then an empty string is returned.
     * 
     * <p>
     * Calling this method with the following Java names will produce these results:
     * 
     * <pre>
     *                     getCarRegistration        -&gt; CarRegistration
     *                     CityMayor -&gt; CityMayor
     *                     isReady -&gt; Ready
     * </pre>
     * 
     */
    public static String javaBaseName(final String javaName) {
        int pos = 0;

        // find first upper case character
        final int len = javaName.length();

        while ((pos < len) && (javaName.charAt(pos) != '_') && Character.isLowerCase(javaName.charAt(pos))) {
            pos++;
        }

        if (pos >= len) {
            return "";
        }

        if (javaName.charAt(pos) == '_') {
            pos++;
        }

        if (pos >= len) {
            return "";
        }

        final String baseName = javaName.substring(pos);
        final char firstChar = baseName.charAt(0);

        if (Character.isLowerCase(firstChar)) {
            return Character.toUpperCase(firstChar) + baseName.substring(1);
        } else {
            return baseName;
        }
    }

    public static String capitalizeName(final String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static boolean startsWith(final String fullMethodName, final String prefix) {
        final int length = prefix.length();
        if (length >= fullMethodName.length()) {
            return false;
        } else {
            final char startingCharacter = fullMethodName.charAt(length);
            return fullMethodName.startsWith(prefix) && Character.isUpperCase(startingCharacter);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
