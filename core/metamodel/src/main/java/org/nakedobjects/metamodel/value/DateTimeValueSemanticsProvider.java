package org.nakedobjects.metamodel.value;

import java.util.Date;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.DateTime;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class DateTimeValueSemanticsProvider extends JavaUtilDateValueSemanticsProviderAbstract {
    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public DateTimeValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public DateTimeValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, DateTime.class, IMMUTABLE, EQUAL_BY_CONTENT, configuration, specificationLoader, runtimeContext);
    }

    @Override
    protected Date dateValue(final Object value) {
        final DateTime date = (DateTime) value;
        return date == null ? null : date.dateValue();
    }

    @Override
    protected Object add(
            final Object original,
            final int years,
            final int months,
            final int days,
            final int hours,
            final int minutes) {
        DateTime date = (DateTime) original;
        date = date.add(years, months, days, hours, minutes);
        return date;
    }

    @Override
    protected Object now() {
        return new DateTime();
    }

    @Override
    protected Object setDate(final Date date) {
        return new DateTime(date);
    }

}

// Copyright (c) Naked Objects Group Ltd.
