package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import java.util.regex.Pattern;

import org.nakedobjects.metamodel.facets.FacetHolder;


public class RegExFacetAnnotation extends RegExFacetAbstract {

    private final Pattern pattern;

    public RegExFacetAnnotation(
            final String validation,
            final String format,
            final boolean caseSensitive,
            final FacetHolder holder) {
        super(validation, format, caseSensitive, holder);
        pattern = Pattern.compile(validation(), patternFlags());
    }

    public String format(final String text) {
        if (text == null) {
            return "<not a string>";
        }
        if (format() == null || format().length() == 0) {
            return text;
        }
        return pattern.matcher(text).replaceAll(format());
    }

    public boolean doesNotMatch(final String text) {
        if (text == null) {
            return true;
        }
        return !pattern.matcher(text).matches();
    }

    private int patternFlags() {
        return !caseSensitive() ? Pattern.CASE_INSENSITIVE : 0;
    }

}

// Copyright (c) Naked Objects Group Ltd.
