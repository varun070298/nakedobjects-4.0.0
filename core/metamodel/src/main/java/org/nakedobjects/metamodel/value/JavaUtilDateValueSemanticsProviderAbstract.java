package org.nakedobjects.metamodel.value;

import java.text.DateFormat;
import java.util.Hashtable;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class JavaUtilDateValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstractTemporal {

    private static final Object DEFAULT_VALUE = null; // no default

    private static Hashtable formats = new Hashtable();
    private static final int TYPICAL_LENGTH = 18;

    static {
        formats.put("iso", createDateFormat("yyyy-MM-dd HH:mm"));
        formats.put("iso_short", createDateFormat("yyyyMMdd'T'HHmm"));
        formats.put("iso_sec", createDateFormat("yyyy-MM-dd HH:mm:ss"));
        formats.put("iso_sec_short", createDateFormat("yyyyMMdd'T'HHmmss"));
        formats.put("iso_milli", createDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        formats.put("iso_milli_short", createDateFormat("yyyyMMdd'T'HHmmssSSS"));
        formats.put(ISO_ENCODING_FORMAT, createDateFormat("yyyyMMdd'T'HHmmssSSS"));
        formats.put("long", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG));
        formats.put("medium", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT));
        formats.put("short", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
    }

    public JavaUtilDateValueSemanticsProviderAbstract(
            final FacetHolder holder,
            final Class<?> adaptedClass,
            final boolean immutable,
            final boolean equalByContent,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super("datetime", holder, adaptedClass, TYPICAL_LENGTH, immutable, equalByContent, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);

        final String formatRequired = configuration.getString(
                ConfigurationConstants.ROOT + "value.format.datetime");
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
        return DATE_AND_TIME;
    }

    // //////////////////////////////////////////////////////////////////
    // temporal-specific stuff
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String defaultFormat() {
        return "medium";
    }

    @Override
    protected Hashtable formats() {
        return formats;
    }

    @Override
    public String toString() {
        return "JavaDateTimeValueSemanticsProvider: " + format;
    }

}
// Copyright (c) Naked Objects Group Ltd.
