package org.nakedobjects.metamodel.value;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class TimeValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstractTemporal {

    private static final Object DEFAULT_VALUE = null; // no default
    private static final int TYPICAL_LENGTH = 6;

    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;

    protected static void initFormats(final Hashtable<String, DateFormat> formats) {
        formats.put("iso", createDateFormat("HH:mm"));
        formats.put("iso_sec", createDateFormat("HH:mm:ss"));
        formats.put("iso_milli", createDateFormat("HH:mm:ss.SSS"));
        formats.put(ISO_ENCODING_FORMAT, createDateFormat("HHmmssSSS"));
        formats.put("long", DateFormat.getTimeInstance(DateFormat.LONG));
        formats.put("medium", DateFormat.getTimeInstance(DateFormat.MEDIUM));
        formats.put("short", DateFormat.getTimeInstance(DateFormat.SHORT));
    }

    public TimeValueSemanticsProviderAbstract(
            final FacetHolder holder,
            final Class<?> adaptedClass,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        super("time", holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration,
                specificationLoader, runtimeContext);

        final String formatRequired = configuration.getString(ConfigurationConstants.ROOT + "value.format.time");
        if (formatRequired == null) {
            format = (DateFormat) formats().get(defaultFormat());
        } else {
            setMask(formatRequired);
        }
    }

    // //////////////////////////////////////////////////////////////////
    // DateValueFacet
    // //////////////////////////////////////////////////////////////////

    @Override
    public int getLevel() {
        return TIME_ONLY;
    }

    // //////////////////////////////////////////////////////////////////
    // temporal-specific stuff
    // //////////////////////////////////////////////////////////////////

    @Override
    protected void clearFields(final Calendar cal) {
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
    }

    @Override
    protected String defaultFormat() {
        return "short";
    }

    @Override
    public String toString() {
        return "TimeValueSemanticsProvider: " + format;
    }

}
// Copyright (c) Naked Objects Group Ltd.
