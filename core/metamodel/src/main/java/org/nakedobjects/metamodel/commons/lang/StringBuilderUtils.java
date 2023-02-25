package org.nakedobjects.metamodel.commons.lang;

public class StringBuilderUtils {

    public static void appendBuf(final StringBuilder buf, final String formatString, final Object... args) {
        buf.append(String.format(formatString, args));
    }

    /**
     * @deprecated - use a {@link StringBuilder} instead!
     */
    @Deprecated
    public static void appendBuf(final StringBuffer buf, final String formatString, final Object... args) {
        buf.append(String.format(formatString, args));
    }

}
