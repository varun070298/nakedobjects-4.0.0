package org.nakedobjects.metamodel.value;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.BigIntegerValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class BigIntegerValueSemanticsProvider extends ValueSemanticsProviderAbstract implements BigIntegerValueFacet {

    private static final int TYPICAL_LENGTH = 19;

    private static Class<? extends Facet> type() {
        return BigIntegerValueFacet.class;
    }

    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;
    private static final Object DEFAULT_VALUE = BigInteger.valueOf(0);

    private final NumberFormat format;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public BigIntegerValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public BigIntegerValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {

        super(type(), holder, BigInteger.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
        format = determineNumberFormat("value.format.int");
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new BigInteger(entry);
        } catch (final NumberFormatException e) {
            throw new TextEntryParseException("Not an integer " + entry, e);
        }
    }

    @Override
    public String titleString(final Object object) {
        return titleString(format, object);
    }

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
        return new BigInteger(data);
    }

    // /////// toString ///////

    @Override
    public String toString() {
        return "BigIntegerValueSemanticsProvider: " + format;
    }

}
// Copyright (c) Naked Objects Group Ltd.
