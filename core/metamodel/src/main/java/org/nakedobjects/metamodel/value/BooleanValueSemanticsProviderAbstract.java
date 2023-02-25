package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.NakedObjectUtils;


public abstract class BooleanValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements BooleanValueFacet {

    private static Class<? extends Facet> type() {
        return BooleanValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 5;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;

    public BooleanValueSemanticsProviderAbstract(
    		final FacetHolder holder, 
    		final Class<?> adaptedClass, 
    		final Object defaultValue,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, defaultValue, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // Parsing
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        if ("true".startsWith(entry.toLowerCase())) {
            return Boolean.TRUE;
        } else if ("false".startsWith(entry.toLowerCase())) {
            return Boolean.FALSE;
        } else {
            throw new TextEntryParseException("Not a logical value " + entry);
        }
    }

    @Override
    public String titleString(final Object value) {
        return value == null ? "" : isSet(value) ? "True" : "False";
    }
    
    @Override
    public String titleStringWithMask(final Object value, final String usingMask) {
        return titleString(value);
    }

    // //////////////////////////////////////////////////////////////////
    // Encode, Decode
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        return isSet(object) ? "T" : "F";
    }

    @Override
    protected Object doRestore(final String data) {
        if (data.length() != 1) {
            throw new NakedObjectException("Invalid data for logical, expected 1 byte, got " + data.length());
        }
        switch (data.charAt(0)) {
        case 'T':
            return Boolean.TRUE;
        case 'F':
            return Boolean.FALSE;
        default:
            throw new NakedObjectException("Invalid data for logical, expected 'T', 'F' or 'N, but got " + data.charAt(0));
        }
    }

    private boolean isSet(final Object value) {
        return ((Boolean) value).booleanValue();
    }

    // //////////////////////////////////////////////////////////////////
    // BooleanValueFacet
    // //////////////////////////////////////////////////////////////////

    public boolean isSet(final NakedObject nakedObject) {
        if (!NakedObjectUtils.exists(nakedObject)) {
            return false;
        }
        final Object object = nakedObject.getObject();
        final Boolean objectAsBoolean = (Boolean) object;
        return objectAsBoolean.booleanValue();
    }

    // /////// toString ///////

    @Override
    public String toString() {
        return "BooleanValueSemanticsProvider";
    }

}
// Copyright (c) Naked Objects Group Ltd.
