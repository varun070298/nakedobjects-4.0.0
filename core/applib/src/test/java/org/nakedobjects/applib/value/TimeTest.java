package org.nakedobjects.applib.value;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TimeTest {
    public static void main(final String[] args) {
        final Date date = new Date(3600000 * 14);

        Locale.setDefault(Locale.KOREA);
        Locale.setDefault(Locale.US);
        Locale.setDefault(Locale.FRANCE);

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        final DateFormat format = DateFormat.getDateTimeInstance();

        System.out.println(date.toString());
        System.out.println(format.format(date));
    }

    private Time time;

    @Before
    public void setUp() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        TestClock.initialize();
        time = new Time(13, 14);
    }

    @Test
    public void testAdd() {
        final Time result = time.add(2, 3);
        assertEquals(17, result.getMinute());
        assertEquals(15, result.getHour());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(time.equals(time));
        assertTrue(time.equals(new Time(13, 14)));
        assertTrue(new Time(13, 14).equals(time));
    }

    @Test
    public void testGetHour() {
        assertEquals(13, time.getHour());
    }

    @Test
    public void testGetMinute() {
        assertEquals(14, time.getMinute());
    }

    @Test
    public void testIsLestThan() throws Exception {
        assertFalse(new Time(8, 17).isLessThan(new Time(8, 17)));
        assertTrue(new Time(8, 16).isLessThan(new Time(8, 17)));
    }

    @Test
    public void testNewWithCurrentTime() {
        final Time expected = new Time(21, 30);
        final Time actual = new Time();
        assertEquals(expected, actual);
    }

    @Test
    public void testSameHourAs() throws Exception {
        assertTrue(new Time(8, 17).sameHourAs(new Time(8, 7)));
        assertFalse(new Time(2, 15).sameHourAs(new Time(8, 17)));
    }

    @Test
    public void testSameMinuteAs() throws Exception {
        assertTrue(new Time(2, 17).sameMinuteAs(new Time(8, 17)));
        assertFalse(new Time(2, 15).sameMinuteAs(new Time(8, 17)));
    }

    @Test
    public void testStartOfHour() {
        assertEquals(new Time(13, 00), time.onTheHour());
    }

    @Test
    public void testTitle() {
        assertEquals("13:14", time.titleString());
    }

    @Test
    public void testToString() {
        assertEquals("13:14", time.toString());
    }

}
// Copyright (c) Naked Objects Group Ltd.
