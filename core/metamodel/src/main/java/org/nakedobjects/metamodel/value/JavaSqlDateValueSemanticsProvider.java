package org.nakedobjects.metamodel.value;

import java.sql.Date;
import java.util.Calendar;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


/**
 * An adapter that handles {@link java.sql.Date} with only date component.
 * 
 * @see JavaUtilDateValueSemanticsProvider
 * @see JavaSqlTimeValueSemanticsProvider
 */
public class JavaSqlDateValueSemanticsProvider extends DateValueSemanticsProviderAbstract {

    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;
    private static final Object DEFAULT_VALUE = null; // no default

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public JavaSqlDateValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public JavaSqlDateValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, Date.class, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    @Override
    protected Object add(
            final Object original,
            final int years,
            final int months,
            final int days,
            final int hours,
            final int minutes) {
        final Date date = (Date) original;
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.AM_PM, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.YEAR, years);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return setDate(cal.getTime());
    }

    @Override
    protected java.util.Date dateValue(final Object value) {
        return (java.util.Date) value;
    }

    @Override
    protected Object setDate(final java.util.Date date) {
        return new Date(date.getTime());
    }

    @Override
    protected Object now() {
        return new Date(Clock.getTime());
    }

}
// Copyright (c) Naked Objects Group Ltd.
