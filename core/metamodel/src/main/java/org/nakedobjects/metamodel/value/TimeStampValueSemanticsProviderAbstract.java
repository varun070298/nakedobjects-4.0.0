package org.nakedobjects.metamodel.value;

import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.applib.value.TimeStamp;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class TimeStampValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstractTemporal {

    private static final Object DEFAULT_VALUE = null; // no default
    private static final int TYPICAL_LENGTH = 12;

    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;

    protected static void initFormats(final Hashtable<String, DateFormat> formats) {
        formats.put("iso", createDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        formats.put(ISO_ENCODING_FORMAT, createDateFormat("yyyyMMdd'T'HHmmssSSS"));
        formats.put("medium", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG));
        formats.put("short", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG));
    }

    public TimeStampValueSemanticsProviderAbstract(
            final FacetHolder holder,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        super("timestamp", holder, TimeStamp.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration,
                specificationLoader, runtimeContext);
        final String formatRequired = configuration.getString(ConfigurationConstants.ROOT + "value.format.timestamp");
        if (formatRequired == null) {
            format = (DateFormat) formats().get(defaultFormat());
        } else {
            setMask(formatRequired);
        }
    }

    @Override
    public int getLevel() {
        return TIMESTAMP;
    }
    
    @Override
    protected Object add(
            final Object original,
            final int years,
            final int months,
            final int days,
            final int hours,
            final int minutes) {
        return original;
    }

    @Override
    protected Date dateValue(final Object value) {
        return new Date(((TimeStamp) value).longValue());
    }

    @Override
    protected String defaultFormat() {
        return "short";
    }

    @Override
    public String toString() {
        return "TimeStampValueSemanticsProvider: " + format;
    }

}

// Copyright (c) Naked Objects Group Ltd.
