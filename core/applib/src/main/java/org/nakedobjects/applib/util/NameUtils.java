package org.nakedobjects.applib.util;

import org.nakedobjects.applib.Identifier;


/**
 * Not public API, provides a number of utilities to represent formal {@link Identifier} names more naturally.
 */
public class NameUtils {
    private static final char SPACE = ' ';

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

    public static String[] naturalNames(final String[] names) {
        final String[] naturalNames = new String[names.length];
        int i = 0;
        for (final String name : names) {
            naturalNames[i++] = NameUtils.naturalName(name);
        }
        return naturalNames;
    }

}
// Copyright (c) Naked Objects Group Ltd.
