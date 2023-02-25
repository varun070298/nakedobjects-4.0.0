package org.nakedobjects.metamodel.commons.lang;

import java.util.Locale;

public class LocaleUtils {

    public static Locale findLocale(final String localeStr) {
        if (localeStr != null) {
            Locale[] availableLocales = Locale.getAvailableLocales();
            for(Locale locale: availableLocales) {
                if (locale.toString().equals(localeStr)) {
                    return locale;
                }
            }
        }
        return null;
    }

}
