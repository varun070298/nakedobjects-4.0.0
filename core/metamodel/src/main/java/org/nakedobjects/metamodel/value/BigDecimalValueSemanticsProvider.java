package org.nakedobjects.metamodel.value;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.BigDecimalValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class BigDecimalValueSemanticsProvider extends ValueSemanticsProviderAbstract implements BigDecimalValueFacet {

    private static final int TYPICAL_LENGTH = 19;

    private static Class<? extends Facet> type() {
        return BigDecimalValueFacet.class;
    }

    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;
    private static final Object DEFAULT_VALUE = new BigDecimal(0);

    private final NumberFormat format;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public BigDecimalValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public BigDecimalValueSemanticsProvider(
    		final FacetHolder holder, 
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, BigDecimal.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
        format = determineNumberFormat("value.format.decimal");
    }


    public void setLocale(Locale l) {
        // TODO Auto-generated method stub
        
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new BigDecimal(entry);
        } catch (final NumberFormatException e) {
            throw new TextEntryParseException("Not an decimal " + entry, e);
        }
    }

    @Override
    public String titleString(final Object object) {
        return titleString(format, object);
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
        // for dotnet compatibility - toString pre 1.3 was equivalent to toPlainString later.
        try {
            final Class<?> type = object.getClass();
            try {
                return (String) type.getMethod("toPlainString", (Class[]) null).invoke(object, (Object[]) null);
            } catch (final NoSuchMethodException nsm) {
                return (String) type.getMethod("toString", (Class[]) null).invoke(object, (Object[]) null);
            }
        } catch (final Exception e) {
            throw new NakedObjectException(e);
        }

    }

    @Override
    protected Object doRestore(final String data) {
        return new BigDecimal(data);
    }

    // /////// toString ///////

    @Override
    public String toString() {
        return "BigDecimalValueSemanticsProvider: " + format;
    }


}
// Copyright (c) Naked Objects Group Ltd.
