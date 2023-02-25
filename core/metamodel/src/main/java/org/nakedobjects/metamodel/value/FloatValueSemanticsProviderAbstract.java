package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.FloatingPointValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class FloatValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements FloatingPointValueFacet {

    public static Class<? extends Facet> type() {
        return FloatingPointValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 12;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;

    private final NumberFormat format;

    public FloatValueSemanticsProviderAbstract(
    		final FacetHolder holder, final Class<?> adaptedClass, final Object defaultValue,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, defaultValue, configuration, specificationLoader, runtimeContext);
        format = determineNumberFormat("value.format.float");
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new Float(format.parse(entry).floatValue());
        } catch (final ParseException e) {
            throw new TextEntryParseException("Not a floating point number " + entry, e);
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
        return new Float(data);
    }

    // //////////////////////////////////////////////////////////////////
    // FloatingPointValueFacet
    // //////////////////////////////////////////////////////////////////

    public Float floatValue(final NakedObject object) {
        return object == null ? null : (Float) object.getObject();
    }

    public NakedObject createValue(final Float value) {
        return getRuntimeContext().adapterFor(value);
    }

    // /////// toString ///////

    @Override
    public String toString() {
        return "FloatValueSemanticsProvider: " + format;
    }


}
// Copyright (c) Naked Objects Group Ltd.
