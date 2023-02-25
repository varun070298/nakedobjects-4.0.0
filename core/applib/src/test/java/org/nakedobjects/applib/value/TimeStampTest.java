package org.nakedobjects.applib.value;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeStampTest {

    private TimeStamp timeStamp;

    @Before
    public void setUp() throws Exception {
        TestClock.initialize();
        timeStamp = new TimeStamp();
    }

    @Test
    public void testCreatesToClocksTime() {
        assertEquals(1061155825000L, timeStamp.longValue());
    }

    @Test
    public void testEqualsTo() {
        final TimeStamp timeStamp2 = new TimeStamp();
        assertFalse(timeStamp2 == timeStamp);

        assertTrue(timeStamp.isEqualTo(timeStamp2));
        assertTrue(timeStamp2.isEqualTo(timeStamp));
    }

    @Test
    public void testLessThan() {
        final TimeStamp timeStamp2 = new TimeStamp(1061155825050L);

        assertTrue(timeStamp.isLessThan(timeStamp2));
        assertFalse(timeStamp2.isLessThan(timeStamp));
    }

}

// Copyright (c) Naked Objects Group Ltd.
