package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.IntegerValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class IntValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements IntegerValueFacet {

    public static Class<? extends Facet> type() {
        return IntegerValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 9;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;

    private final NumberFormat format;

    public IntValueSemanticsProviderAbstract(
    		final FacetHolder holder, final Class<?> adaptedClass, final Object defaultValue,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, defaultValue, configuration, specificationLoader, runtimeContext);
        format = determineNumberFormat("value.format.int");
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new Integer(format.parse(entry).intValue());
        } catch (final ParseException e) {
            throw new TextEntryParseException("Not an whole number " + entry, e);
        }
    }

    @Override
    public String titleString(final Object value) {
        return titleString(format, value);
    }

    @Override
    public String titleStringWithMask(final Object value, final String usingMask) {
        return titleString(new DecimalFormat(usingMask), value);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        return object.toString();
    }

    @Override
    protected Object doRestore(final String data) {
        return new Integer(data);
    }

    // //////////////////////////////////////////////////////////////////
    // IntegerValueFacet
    // //////////////////////////////////////////////////////////////////

    public Integer integerValue(final NakedObject object) {
        return (Integer) (object == null ? null : object.getObject());
    }

    public NakedObject createValue(final Integer value) {
        return value == null ? null : getRuntimeContext().adapterFor(value);
    }

    // /////// toString ///////

    @Override
    public String toString() {
        return "IntegerValueSemanticsProvider: " + format;
    }

    

}
// Copyright (c) Naked Objects Group Ltd.
