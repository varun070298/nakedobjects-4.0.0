package org.nakedobjects.plugins.headless.viewer.internal.util;



public final class Constants {
    private Constants() {}

    public static final String PREFIX_CHOICES = "choices";
    public static final String PREFIX_DEFAULT = "default";
    public static final String PREFIX_HIDE = "hide";
    public static final String PREFIX_DISABLE = "disable";
    public static final String PREFIX_VALIDATE_REMOVE_FROM = "validateRemoveFrom";
    public static final String PREFIX_VALIDATE_ADD_TO = "validateAddTo";
    public static final String PREFIX_VALIDATE = "validate";
    public static final String PREFIX_REMOVE_FROM = "removeFrom";
    public static final String PREFIX_ADD_TO = "addTo";
    public static final String PREFIX_MODIFY = "modify";
    public static final String PREFIX_CLEAR = "clear";
    public static final String PREFIX_SET = "set";
    public static final String PREFIX_GET = "get";

    public final static String TITLE_METHOD_NAME = "title";
    public final static String TO_STRING_METHOD_NAME = "toString";

    /**
     * Cannot invoke methods with these prefixes.
     */
    public final static String[] INVALID_PREFIXES = { PREFIX_MODIFY, PREFIX_CLEAR, PREFIX_DISABLE, PREFIX_VALIDATE,
            PREFIX_VALIDATE_ADD_TO, PREFIX_VALIDATE_REMOVE_FROM, PREFIX_HIDE, };

    public final static String[] PROPERTY_PREFIXES = { PREFIX_GET, PREFIX_SET, PREFIX_MODIFY, PREFIX_CLEAR, PREFIX_DISABLE,
            PREFIX_VALIDATE, PREFIX_HIDE, PREFIX_DEFAULT, PREFIX_CHOICES };
    public final static String[] COLLECTION_PREFIXES = { PREFIX_GET, PREFIX_SET, PREFIX_ADD_TO, PREFIX_REMOVE_FROM,
            PREFIX_DISABLE, PREFIX_VALIDATE_ADD_TO, PREFIX_VALIDATE_REMOVE_FROM, PREFIX_HIDE, PREFIX_DEFAULT, PREFIX_CHOICES };
    public final static String[] ACTION_PREFIXES = { PREFIX_VALIDATE, PREFIX_DISABLE, PREFIX_HIDE, PREFIX_DEFAULT,
            PREFIX_CHOICES, };


}
