package org.nakedobjects.applib.value;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.nakedobjects.applib.clock.Clock;


public class TestClock extends Clock {

    public static final TimeZone timeZone;

    public static void initialize() {
        new TestClock();

        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(timeZone);
    }

    private TestClock() {
        super();
    }

    static {
        timeZone = TimeZone.getTimeZone("Etc/UTC");
    }

    /**
     * Always return the time as 2003/8/17 21:30:25
     */
    @Override
    protected long time() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(timeZone);

        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.YEAR, 2003);
        c.set(Calendar.MONTH, 7);
        c.set(Calendar.DAY_OF_MONTH, 17);

        c.set(Calendar.HOUR_OF_DAY, 21);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 25);

        return c.getTime().getTime();
    }

}
// Copyright (c) Naked Objects Group Ltd.
