package org.nakedobjects.metamodel.commons.debug;

public final class Debug {
    private static final String SPACES = "                                                                                                            ";
    private static final int NO_OF_SPACES = SPACES.length();

    public static String indentString(final int indentSpaces) {
        return SPACES.substring(0, Math.min(NO_OF_SPACES, indentSpaces));
    }
}
// Copyright (c) Naked Objects Group Ltd.
