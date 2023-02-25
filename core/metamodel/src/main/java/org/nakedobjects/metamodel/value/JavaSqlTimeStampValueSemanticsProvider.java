package org.nakedobjects.metamodel.value;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class JavaSqlTimeStampValueSemanticsProvider extends TimeStampValueSemanticsProviderAbstract {

    public static final boolean isAPropertyDefaultFacet() {
        return PropertyDefaultFacet.class.isAssignableFrom(JavaSqlTimeStampValueSemanticsProvider.class);
    }

    private static Hashtable formats = new Hashtable();

    static {
        initFormats(formats);
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public JavaSqlTimeStampValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public JavaSqlTimeStampValueSemanticsProvider(
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
        return new Date(((Timestamp) value).getTime());
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
        return new Timestamp(date.getTime());
    }

}

// Copyright (c) Naked Objects Group Ltd.
