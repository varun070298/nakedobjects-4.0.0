package org.nakedobjects.metamodel.commons.names;

public class NameConvertorUtils {
    private static final char SPACE = ' ';

    /**
     * Return a lower case, non-spaced version of the specified name.
     */
    public static String simpleName(final String name) {
        final int len = name.length();
        final StringBuffer sb = new StringBuffer(len);
        for (int pos = 0; pos < len; pos++) {
            final char ch = name.charAt(pos);
            if (ch == ' ') {
                continue;
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }

    /**
     * Returns a word spaced version of the specified name, so there are spaces between the words, where each
     * word starts with a capital letter. E.g., "NextAvailableDate" is returned as "Next Available Date".
     */
    public static String naturalName(final String name) {

        final int length = name.length();

        if (length <= 1) {
            return name.toUpperCase();// ensure first character is upper case
        }

        final StringBuffer naturalName = new StringBuffer(length);

        char previousCharacter;
        char character = Character.toUpperCase(name.charAt(0));// ensure first character is upper case
        naturalName.append(character);
        char nextCharacter = name.charAt(1);

        for (int pos = 2; pos < length; pos++) {
            previousCharacter = character;
            character = nextCharacter;
            nextCharacter = name.charAt(pos);

            if (previousCharacter != SPACE) {
                if (Character.isUpperCase(character) && !Character.isUpperCase(previousCharacter)) {
                    naturalName.append(SPACE);
                }
                if (Character.isUpperCase(character) && Character.isLowerCase(nextCharacter)
                        && Character.isUpperCase(previousCharacter)) {
                    naturalName.append(SPACE);
                }
                if (Character.isDigit(character) && !Character.isDigit(previousCharacter)) {
                    naturalName.append(SPACE);
                }
            }
            naturalName.append(character);
        }
        naturalName.append(nextCharacter);
        return naturalName.toString();
    }

    public static String pluralName(final String name) {
        String pluralName;
        if (name.endsWith("y")) {
            pluralName = name.substring(0, name.length() - 1) + "ies";
        } else if (name.endsWith("s") || name.endsWith("x")) {
            pluralName = name + "es";
        } else {
            pluralName = name + 's';
        }
        return pluralName;
    }

}
// Copyright (c) Naked Objects Group Ltd.
