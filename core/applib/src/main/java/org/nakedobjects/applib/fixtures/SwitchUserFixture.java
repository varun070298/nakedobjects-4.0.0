package org.nakedobjects.applib.fixtures;

/**
 * Sole purpose is to switch the current user while object fixtures are being installed.
 * 
 * <p>
 * An alternative is to switch user using the {@link AbstractFixture#switchUser(String, String...) switchUser} method.
 * 
 * <p>
 * Note that (unlike the otherwise similar {@link DateFixture}) the last user switched to is
 * <i>not</i> used as the logon fixture.  If you want to automatically logon as some user,
 * use the {@link LogonFixture}.
 * 
 * @see DateFixture
 * @see LogonFixture
 */
public class SwitchUserFixture extends AbstractFixture {

    public SwitchUserFixture(final int year, final int month, final int day, final int hour, final int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minutes;
    }

    public SwitchUserFixture(final int year, final int month, final int day) {
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
