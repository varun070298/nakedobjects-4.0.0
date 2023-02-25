package org.nakedobjects.applib.fixtures;

/**
 * Sole purpose is to set the date/time while object fixtures are being installed.
 * 
 * <p>
 * An alternative is to change the date using {@link AbstractFixture#setDate(int, int, int)} and
 * {@link AbstractFixture#setTime(int, int)}.
 * 
 * <p>
 * Note that the last date set <i>will</i> remain in force for the application itself.  To revert to the
 * current time, have a fixture at the end call {@link #resetClock()}.
 * 
 * @see SwitchUserFixture
 */
public class DateFixture extends AbstractFixture {

    public DateFixture(final int year, final int month, final int day, final int hour, final int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minutes;
    }

    public DateFixture(final int year, final int month, final int day) {
        this(year, month, day, 0, 0);
    }

    private final int year;

    public int getYear() {
        return year;
    }
    private final int month;

    public int getMonth() {
        return month;
    }
    private final int day;

    public int getDay() {
        return day;
    }
    private final int hour;

    public int getHour() {
        return hour;
    }
    private final int minute;

    public int getMinute() {
        return minute;
    }

    @Override
    public void install() {
        setDate(year, month, day);
        setTime(hour, minute);
    }

}
