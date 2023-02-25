package org.nakedobjects.applib.value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DateTest {

    private Date actual;

    @Before
    public void setUp() throws Exception {
        TestClock.initialize();
        actual = new Date(2000, 3, 14);
    }

    @Test
    public void testGetDay() {
        assertEquals(14, actual.getDay());
    }

    @Test
    public void testGetMonth() {
        assertEquals(3, actual.getMonth());
    }

    @Test
    public void testGetYear() {
        assertEquals(2000, actual.getYear());
    }

    @Test
    public void testAdd() {
        final Date resultDate = actual.add(1, 2, 3);
        assertEquals(17, resultDate.getDay());
        assertEquals(5, resultDate.getMonth());
        assertEquals(2001, resultDate.getYear());
    }

    @Test
    public void testDate() {
        actual = new Date(2001, 3, 7);
        assertEquals("day", 7, actual.getDay());
        assertEquals("month", 3, actual.getMonth());
        assertEquals("year", 2001, actual.getYear());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(actual.equals(actual));
        assertTrue(new Date(2003, 8, 17).equals(new Date()));
        assertTrue(actual.equals(new Date(2000, 3, 14)));
    }

    @Test
    public void testIsLestThan() throws Exception {
        assertFalse(new Date(2003, 8, 17).isLessThan(new Date(2003, 8, 17)));
        assertTrue(new Date(2003, 8, 16).isLessThan(new Date(2003, 8, 17)));
    }

    @Test
    public void testSameDayOfWeekAs() throws Exception {
        assertTrue(new Date(2000, 2, 17).sameDayOfWeekAs(new Date(2003, 8, 7))); // Thursday
        assertFalse(new Date(2000, 2, 15).sameDayOfWeekAs(new Date(2003, 8, 17))); // Tues &
    }

    @Test
    public void testSameDayOfMonthAs() throws Exception {
        assertTrue(new Date(2000, 2, 17).sameDayOfMonthAs(new Date(2003, 8, 17)));
        assertFalse(new Date(2000, 2, 15).sameDayOfMonthAs(new Date(2003, 8, 17)));
    }

    @Test
    public void testSameDayOfYearAs() throws Exception {
        assertTrue(new Date(2001, 8, 17).sameDayOfYearAs(new Date(2003, 8, 17)));
        assertTrue(new Date(1999, 3, 1).sameDayOfYearAs(new Date(2000, 2, 29))); // leap year
        assertFalse(new Date(2001, 3, 1).sameDayOfYearAs(new Date(2000, 3, 2)));
    }

    @Test
    public void testSameWeekAs() throws Exception {
        assertFalse(new Date(2000, 2, 15).sameWeekAs(new Date(2000, 2, 12))); // Tue, week 7 and Sat, week 6
        assertTrue(new Date(2001, 2, 16).sameWeekAs(new Date(2002, 2, 11))); // Tue, week 7, and Thu, week
        // 7
    }

    @Test
    public void testSameMonthAs() throws Exception {
        assertTrue(new Date(2000, 8, 15).sameMonthAs(new Date(2003, 8, 17)));
        assertFalse(new Date(2003, 2, 17).sameMonthAs(new Date(2003, 8, 17)));
    }

    @Test
    public void testSameYearAs() throws Exception {
        assertTrue(new Date(2003, 2, 15).sameYearAs(new Date(2003, 8, 17)));
        assertFalse(new Date(2000, 2, 15).sameYearAs(new Date(2003, 8, 17)));
    }

    @Test
    public void testDateValue() {
        final Date date = new Date(1970, 1, 1);
        assertEquals(1970, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(1, date.getDay());
        final long time = date.dateValue().getTime();
        assertEquals(1000 * 60 * 60 * 24 * 0, time);
    }

    @Test
    public void testStartOfYear() {
        assertEquals(new Date(2000, 1, 1), actual.startOfYear());
    }

    @Test
    public void testStartOfMonth() {
        assertEquals(new Date(2000, 3, 1), actual.startOfMonth());
    }

    @Test
    public void testStartOfWeek() {
        assertEquals(new Date(2000, 3, 13), actual.startOfWeek());
        assertEquals(new Date(2000, 2, 28), new Date(2000, 3, 2).startOfWeek());
    }

    @Test
    public void testNewWithTodaysDate() {
        final Date actual = new Date();
        final Date expected = new Date(2003, 8, 17);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        assertEquals("2000-3-14", actual.toString());
    }

}
// Copyright (c) Naked Objects Group Ltd.
