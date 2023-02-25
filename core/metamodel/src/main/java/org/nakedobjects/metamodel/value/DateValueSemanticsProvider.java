package org.nakedobjects.metamodel.value;

import java.util.Date;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class DateValueSemanticsProvider extends DateValueSemanticsProviderAbstract {

    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;
    private static final Object DEFAULT_VALUE = null; // new org.nakedobjects.applib.value.Date(2007,1,1);

    // // no default

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public DateValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public DateValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, org.nakedobjects.applib.value.Date.class, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    @Override
    protected Object add(
            final Object original,
            final int years,
            final int months,
            final int days,
            final int hours,
            final int minutes) {
        final org.nakedobjects.applib.value.Date date = (org.nakedobjects.applib.value.Date) original;
        return date.add(years, months, days);
    }

    @Override
    protected org.nakedobjects.applib.value.Date now() {
        return new org.nakedobjects.applib.value.Date();
    }

    @Override
    protected Date dateValue(final Object value) {
        return ((org.nakedobjects.applib.value.Date) value).dateValue();
    }

    @Override
    protected Object setDate(final Date date) {
        return new org.nakedobjects.applib.value.Date(date);
    }
}
// Copyright (c) Naked Objects Group Ltd.
