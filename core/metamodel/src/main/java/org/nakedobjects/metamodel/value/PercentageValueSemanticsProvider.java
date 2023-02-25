package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.Percentage;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.FloatingPointValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class PercentageValueSemanticsProvider extends ValueSemanticsProviderAbstract implements FloatingPointValueFacet {

    private static final NumberFormat PERCENTAGE_FORMAT = NumberFormat.getPercentInstance();
    private static final NumberFormat DECIMAL_FORMAT = NumberFormat.getNumberInstance();

    private static final boolean IMMUTABLE = true;
    private static final boolean EQUAL_BY_CONTENT = true;
    private static final Object DEFAULT_VALUE = new Percentage(0.0f);

    public static Class<? extends Facet> type() {
        return FloatingPointValueFacet.class;
    }

    private NumberFormat format = PERCENTAGE_FORMAT;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public PercentageValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public PercentageValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(type(), holder, Percentage.class, 12, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
        
        final String formatRequired = configuration.getString(
                ConfigurationConstants.ROOT + "value.format.percentage");
        if (formatRequired == null) {
            format = PERCENTAGE_FORMAT;
        } else {
            format = new DecimalFormat(formatRequired);
        }
    }

    // //////////////////////////////////////////////////////////////////
    // Parser
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String text) {
        try {
            return new Percentage(new Float(format.parse(text).floatValue()));
        } catch (final ParseException e) {
            try {
                return new Percentage(new Float(DECIMAL_FORMAT.parse(text).floatValue()));
            } catch (final ParseException ee) {
                throw new TextEntryParseException("Not a number " + text, ee);
            }
        }
    }

    @Override
    public String titleString(final Object value) {
        return titleString(format, value);
    }

    private String titleString(final NumberFormat formatter, final Object value) {
        return value == null ? "" : format.format(((Percentage) value).floatValue());
    }

    public String titleStringWithMask(final Object value, final String usingMask) {
        return titleString(new DecimalFormat(usingMask), value);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        final Percentage per = (Percentage) object;
        return String.valueOf(per.floatValue());
    }

    @Override
    protected Object doRestore(final String data) {
        return new Percentage(Float.valueOf(data).floatValue());
    }

    // //////////////////////////////////////////////////////////////////
    // FloatingPointValueFacet
    // //////////////////////////////////////////////////////////////////

    public Float floatValue(final NakedObject object) {
        final Percentage per = (Percentage) object.getObject();
        return new Float(per.floatValue());
    }

    public NakedObject createValue(final Float value) {
        return getRuntimeContext().adapterFor(value);
    }

    // //////////////////////////////////////////////////////////////////
    // PropertyDefaultFacet
    // //////////////////////////////////////////////////////////////////

    public Object getDefault(final NakedObject inObject) {
        return Float.valueOf(0.0f);
    }

    // //// toString ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "PercentageValueSemanticsProvider: " + format;
    }

    

}

// Copyright (c) Naked Objects Group Ltd.
