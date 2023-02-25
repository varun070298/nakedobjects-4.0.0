package org.nakedobjects.metamodel.commons.locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;


public class PrintLocale {
    public static void main(final String[] args) {
        if (args.length >= 2) {
            final String localeSetting = args[0];
            System.out.println("Setting Locale to " + localeSetting + "\n");
            Locale.setDefault(new Locale(localeSetting));

            final String timezoneSetting = args[1];
            System.out.println("Setting TimeZone to " + timezoneSetting + "\n");
            TimeZone.setDefault(TimeZone.getTimeZone(timezoneSetting));
        }

        final Locale locale = Locale.getDefault();
        System.out.println("Locale");
        System.out.println("Code: " + locale.toString());
        try {
            System.out.println("Country: " + locale.getISO3Country());
        } catch (final MissingResourceException e) {
            System.out.println("Country: " + e.getMessage());
        }
        try {
            System.out.println("Language: " + locale.getISO3Language());
        } catch (final MissingResourceException e) {
            System.out.println("Language: " + e.getMessage());
        }

        System.out.println("\nTimezone");
        final TimeZone timezone = TimeZone.getDefault();
        System.out.println("Code: " + timezone.getID());
        System.out.println("Name: " + timezone.getDisplayName());
        System.out.println("Offset: " + timezone.getRawOffset() / (1000 * 60 * 60));
        System.out.println("DST: " + timezone.getDSTSavings() / (1000 * 60 * 60));
    }
}

// Copyright (c) Naked Objects Group Ltd.
