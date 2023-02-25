package org.nakedobjects.metamodel.value;

import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.TimeStamp;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class TimeStampValueSemanticsProvider extends TimeStampValueSemanticsProviderAbstract {

    public static final boolean isAPropertyDefaultFacet() {
        return PropertyDefaultFacet.class.isAssignableFrom(TimeStampValueSemanticsProvider.class);
    }

    private static Hashtable formats = new Hashtable();

    static {
        initFormats(formats);
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public TimeStampValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public TimeStampValueSemanticsProvider(
            final FacetHolder holder,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        super(holder, configuration, specificationLoader, runtimeContext);
    }

    // //////////////////////////////////////////////////////////////////
    // temporal-specific stuff
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Date dateValue(final Object value) {
        return new Date(((TimeStamp) value).longValue());
    }

    @Override
    protected Hashtable formats() {
        return formats;
    }

    @Override
    protected Object now() {
        throw new InvalidEntryException("Can't change a timestamp.");
    }

    @Override
    protected Object setDate(final Date date) {
        return new TimeStamp(date.getTime());
    }

}

// Copyright (c) Naked Objects Group Ltd.
