package org.nakedobjects.applib.value;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.nakedobjects.applib.annotation.Value;
import org.nakedobjects.applib.clock.Clock;


/**
 * Value object representing a time value.
 * 
 * <p>
 * TODO: other methods to implement:
 * <ul>
 * <li>comparison methods</li>
 * <li>sameHourAs() hour ==hour sameMinuteAs() minutes = minutes sameTimeAs(hour, min) hour == hour & minutes ==
 * minutes</li>
 * <li>withinNextTimePeriod(int hours, int minutes); withinTimePeriod(Date d, int hours, int minutes)</li>
 * <li>withinPreviousTimePeriod(int hours, int minutes); d.hour >= this.hour >= d.hour + hours & d.minutes >=
 * this.minutes >= d.minutes + minutes</li>
 * </ul>
 */
@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.TimeValueSemanticsProvider")
public class Time extends Magnitude {
    private static final long serialVersionUID = 1L;
    public static final int MINUTE = 60;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;
    private static final DateFormat SHORT_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);
    private static final long zero;
    private static final TimeZone UtcTimeZone;
    static {
        UtcTimeZone = TimeZone.getTimeZone("Etc/UTC");

        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(UtcTimeZone);
        // set to 1-Jan-1970 00:00:00 (the epoch)
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.HOUR);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1970);
        zero = cal.getTime().getTime();

        SHORT_FORMAT.setTimeZone(UtcTimeZone);
    }

    static long getZero() {
        return zero / 1000;
    }

    private final Date date;

    /**
     * Create a Time object set to the current time.
     */
    public Time() {
        final Calendar cal = createCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(new Date(Clock.getTime()));

        date = time(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    /**
     * Create a Time object for storing a time with the time set to the specified hours and minutes.
     */
    public Time(final int hour, final int minute) {
        date = time(hour, minute);
    }

    private Date time(final int hour, final int minute) {
        checkTime(hour, minute, 0);

        final Calendar cal = createCalendar();
        cal.setTimeZone(UtcTimeZone);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1970);
        return cal.getTime();
    }

    /**
     * Create a Time object for storing a time with the time set to the specified time of the Java Date
     * object.
     */
    public Time(final Date date) {
        final Calendar localTime = createCalendar();
        // localTime.setTimeZone(TimeZone.getDefault());
        localTime.setTimeZone(UtcTimeZone);
        localTime.setTime(date);

        this.date = time(localTime.get(Calendar.HOUR_OF_DAY), localTime.get(Calendar.MINUTE));
    }

    /**
     * Add the specified hours and minutes to this time value, returned as a new Time object.
     */
    public Time add(final int hours, final int minutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return createTime(cal.getTime());
    }

    private void checkTime(final int hour, final int minute, final int second) {
        if ((hour < 0) || (hour > 23)) {
            throw new IllegalArgumentException("Hour must be in the range 0 - 23 inclusive");
        }

        if ((minute < 0) || (minute > 59)) {
            throw new IllegalArgumentException("Minute must be in the range 0 - 59 inclusive");
        }

        if ((second < 0) || (second > 59)) {
            throw new IllegalArgumentException("Second must be in the range 0 - 59 inclusive");
        }
    }

    /**
     * Returns a Calendar object with the irrelevant field (determined by this objects type) set to zero.
     */
    private Calendar createCalendar() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(UtcTimeZone);

        // clear all aspects of the time that are not used
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.HOUR);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1970);

        return cal;
    }

    protected Time createTime(final int hours, final int minutes) {
        return new Time(hours, minutes);
    }

    protected Time createTime(final Date date) {
        return new Time(date);
    }

    public java.util.Date dateValue() {
        return (date == null) ? null : date;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Time)) {
            return false;
        }
        final Time object = (Time) obj;
        return object.date.equals(date);
    }

    public int getHour() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(UtcTimeZone);
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(UtcTimeZone);
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * returns true if the time of this object has the same value as the specified time
     */
    @Override
    public boolean isEqualTo(final Magnitude time) {
        if (time instanceof Time) {
            return (date == null) ? false : (date.equals(((Time) time).date));
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * returns true if the time of this object is earlier than the specified time
     */
    @Override
    public boolean isLessThan(final Magnitude time) {
        if (time instanceof Time) {
            return (date != null) && date.before(((Time) time).date);
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * The number of seconds since midnight.
     */
    public long longValue() {
        return date.getTime() / 1000;
    }

    public String titleString() {
        return (date == null) ? "" : SHORT_FORMAT.format(date);
    }

    @Override
    public String toString() {
        return getHour() + ":" + getMinute();

    }

    public boolean sameHourAs(final Time time) {
        return getHour() == time.getHour();
    }

    public boolean sameMinuteAs(final Time time) {
        return getMinute() == time.getMinute();
    }

    public Time onTheHour() {
        return createTime(getHour(), 0);
    }
}
// Copyright (c) Naked Objects Group Ltd.
