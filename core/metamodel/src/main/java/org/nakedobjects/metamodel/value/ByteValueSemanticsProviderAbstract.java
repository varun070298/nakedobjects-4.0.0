package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.ByteValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class ByteValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements ByteValueFacet {

    private static Class<? extends Facet> type() {
        return ByteValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 4;
    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;

    private final NumberFormat format;

    public ByteValueSemanticsProviderAbstract(
    		final FacetHolder holder, 
    		final Class<?> adaptedClass, 
    		final Object defaultValue,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, defaultValue, configuration, specificationLoader, runtimeContext);
        format = determineNumberFormat("value.format.byte");
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new Byte(format.parse(entry).byteValue());
        } catch (final ParseException e) {
            throw new TextEntryParseException("Not a number " + entry, e);
        }
    }

    @Override
    public String titleString(final Object value) {
        return titleString(format, value);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    public String doEncode(final Object object) {
        return object.toString();
    }

    @Override
    protected Object doRestore(final String data) {
        return new Byte(data);
    }
    
    @Override
    public String titleStringWithMask(final Object value, final String usingMask) {
        return titleString(new DecimalFormat(usingMask), value);
    }

    // //////////////////////////////////////////////////////////////////
    // ByteValueFacet
    // //////////////////////////////////////////////////////////////////

    public Byte byteValue(final NakedObject object) {
        return (Byte) object.getObject();
    }

    public NakedObject createValue(final Byte value) {
        return getRuntimeContext().adapterFor(value);
    }

    // ///// toString ///////

    @Override
    public String toString() {
        return "ByteValueSemanticsProvider: " + format;
    }



}
// Copyright (c) Naked Objects Group Ltd.
