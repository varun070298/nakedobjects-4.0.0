package org.nakedobjects.application.system;

import org.nakedobjects.application.Clock;
import org.nakedobjects.application.valueholder.Date;
import org.nakedobjects.application.valueholder.DateTime;
import org.nakedobjects.application.valueholder.Time;
import org.nakedobjects.application.valueholder.TimeStamp;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class TestClock implements Clock {
    private static final TimeZone timeZone;
    static {
        timeZone = TimeZone.getTimeZone("GMT");
        Locale.setDefault(Locale.UK);
    }
    
    public TestClock() {
        Date.setClock(this);
        Time.setClock(this);
        DateTime.setClock(this);
        TimeStamp.setClock(this);
    }

    /**
     * Always return the time as 2003/8/17 21:30:25
     */
    public long getTime() {
        Calendar c = Calendar.getInstance();
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
