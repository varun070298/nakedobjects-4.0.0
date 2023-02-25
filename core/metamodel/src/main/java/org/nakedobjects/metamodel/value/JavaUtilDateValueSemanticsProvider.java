package org.nakedobjects.metamodel.value;

import java.util.Calendar;
import java.util.Date;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


/**
 * An adapter that handles {@link java.util.Date} as both a date AND time component.
 * 
 * @see JavaSqlDateValueSemanticsProvider
 * @see JavaSqlTimeValueSemanticsProvider
 */
public class JavaUtilDateValueSemanticsProvider extends JavaUtilDateValueSemanticsProviderAbstract {

    private static final boolean IMMUTABLE = false;
    private static final boolean EQUAL_BY_CONTENT = false;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public JavaUtilDateValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public JavaUtilDateValueSemanticsProvider(
    		final FacetHolder holder,
            final NakedObjectConfiguration configuration, 
            final SpecificationLoader specificationLoader, 
            final RuntimeContext runtimeContext) {
        super(holder, Date.class, IMMUTABLE, EQUAL_BY_CONTENT, configuration, specificationLoader, runtimeContext);
    }

    @Override
    protected Date dateValue(final Object value) {
        return value == null ? null : (Date) value;
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
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.YEAR, years);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.HOUR, hours);
        cal.add(Calendar.MINUTE, minutes);

        return setDate(cal.getTime());
    }

    @Override
    protected Object now() {
        return new Date(Clock.getTime());
    }

    @Override
    protected Object setDate(final Date date) {
        return date;
    }
}
// Copyright (c) Naked Objects Group Ltd.
