package org.nakedobjects.metamodel.value;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


/**
 * Treats {@link java.sql.Time} as a time-only value type.
 * 
 */
public class JavaSqlTimeValueSemanticsProvider extends TimeValueSemanticsProviderAbstract {
    private static Hashtable formats = new Hashtable();

    static {
        initFormats(formats);
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public JavaSqlTimeValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public JavaSqlTimeValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, java.sql.Time.class, configuration, specificationLoader, runtimeContext);
    }

    @Override
    public Object add(final Object original, final int years, final int months, final int days, final int hours, final int minutes) {
        final java.sql.Time time = (Time) original;
        final Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.HOUR, hours);
        cal.add(Calendar.MINUTE, minutes);

        return setDate(cal.getTime());
    }

    @Override
    public java.util.Date dateValue(final Object object) {
        final java.sql.Time time = (Time) object;
        return time == null ? null : new java.util.Date(time.getTime());
    }

    @Override
    protected Hashtable formats() {
        return formats;
    }

    @Override
    protected Object now() {
        return new java.sql.Time(Clock.getTime());
    }

    @Override
    protected Object setDate(final Date date) {
        return new java.sql.Time(date.getTime());
    }

}
// Copyright (c) Naked Objects Group Ltd.
