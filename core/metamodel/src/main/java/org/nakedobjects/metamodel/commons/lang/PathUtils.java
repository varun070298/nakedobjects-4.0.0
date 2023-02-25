package org.nakedobjects.metamodel.commons.lang;

public final class PathUtils {

    private PathUtils() {}

    public static String combine(final String path, final String suffix) {
        if (isEmpty(path) && isEmpty(suffix)) {
            return "";
        }
        if (isEmpty(path)) {
            return suffix;
        }
        if (isEmpty(suffix)) {
            return path;
        }
        return path + "/" + suffix;
    }


    private static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

}
