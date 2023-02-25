package org.nakedobjects.metamodel.examples.facets.jsr303;

import javax.validation.Constraint;


public class CustomPatternValidator implements Constraint<CustomPattern> {
    private java.util.regex.Pattern pattern;

    public void initialize(final CustomPattern params) {
        pattern = java.util.regex.Pattern.compile(params.regex(), params.flags());
    }

    public boolean isValid(final Object ovalue) {
        if (ovalue == null) {
            return true;
        }
        if (!(ovalue instanceof String)) {
            return false;
        }
        final String value = (String) ovalue;
        return pattern.matcher(value).matches();
    }
}
