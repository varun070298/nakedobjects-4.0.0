package org.nakedobjects.metamodel.value;

import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.Time;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class TimeValueSemanticsProvider extends TimeValueSemanticsProviderAbstract {
    private static Hashtable formats = new Hashtable();

    static {
        initFormats(formats);
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public TimeValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public TimeValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, org.nakedobjects.applib.value.Time.class, configuration, specificationLoader, runtimeContext);
    }

    // private Time time;

    @Override
    protected Hashtable formats() {
        return formats;
    }

    @Override
    protected boolean ignoreTimeZone() {
        return true;
    }

    @Override
    protected Object add(
            final Object original,
            final int years,
            final int months,
            final int days,
            final int hours,
            final int minutes) {
        Time time = (Time) original;
        time = time.add(hours, minutes);
        return time;
    }

    @Override
    protected Date dateValue(final Object object) {
        final Time time = (Time) object;
        return time == null ? null : time.dateValue();
    }

    @Override
    protected Object now() {
        return new Time();
    }

    @Override
    protected Object setDate(final Date date) {
        return new Time(date);
    }

}
// Copyright (c) Naked Objects Group Ltd.
