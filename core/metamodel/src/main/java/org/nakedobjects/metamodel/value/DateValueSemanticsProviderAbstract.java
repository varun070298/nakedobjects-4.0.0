package org.nakedobjects.metamodel.value;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class DateValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstractTemporal {

    private static Hashtable<String,DateFormat> formats = new Hashtable<String,DateFormat>();

    static {
        formats.put("iso", createDateFormat("yyyy-MM-dd"));
        formats.put(ISO_ENCODING_FORMAT, createDateFormat("yyyyMMdd"));
        formats.put("long", DateFormat.getDateInstance(DateFormat.LONG));
        formats.put("medium", DateFormat.getDateInstance(DateFormat.MEDIUM));
        formats.put("short", DateFormat.getDateInstance(DateFormat.SHORT));
    }

    public DateValueSemanticsProviderAbstract(
            final FacetHolder holder,
            final Class<?> adaptedClass,
            final boolean immutable,
            final boolean equalByContent,
            final Object defaultValue,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super("date", holder, adaptedClass, 12, immutable, equalByContent, defaultValue, configuration, specificationLoader, runtimeContext);

        final String formatRequired = configuration.getString(ConfigurationConstants.ROOT + "value.format.date");
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
        return DATE_ONLY;
    }

    // //////////////////////////////////////////////////////////////////
    // temporal-specific stuff
    // //////////////////////////////////////////////////////////////////

    @Override
    protected void clearFields(final Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.AM_PM, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    @Override
    protected String defaultFormat() {
        return "medium";
    }

    @Override
    protected boolean ignoreTimeZone() {
        return true;
    }

    @Override
    protected Hashtable<String,DateFormat> formats() {
        return formats;
    }

    @Override
    public String toString() {
        return "DateValueSemanticsProvider: " + format;
    }

}
// Copyright (c) Naked Objects Group Ltd.
