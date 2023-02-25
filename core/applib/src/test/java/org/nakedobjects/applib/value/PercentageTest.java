package org.nakedobjects.applib.value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;


public class PercentageTest {
    Percentage p1;
    Percentage p2;
    Percentage p3;

    @Before
    public void setUp() throws Exception {
        p1 = new Percentage(10.5f);
        p2 = new Percentage(10.5f);
        p3 = new Percentage(12.0f);
    }

    @Test
    public void testEquals() {
        assertEquals(p1, p2);
        assertNotSame(p1, p2);
        assertFalse(p1.equals(p3));
    }

    @Test
    public void testAddFloat() {
        final Percentage p4 = p1.add(10.0f);
        assertEquals(20.5f, p4.floatValue(), 0.0f);
    }

    @Test
    public void testAddPercentage() {
        final Percentage p4 = p1.add(p3);
        assertEquals(22.5f, p4.floatValue(), 0.0f);
    }

    @Test
    public void testIsEqualTo() {
        assertTrue(p1.isEqualTo(p2));
        assertFalse(p1.isEqualTo(p3));
    }

    @Test
    public void testIsLessThan() {
        assertTrue(p1.isLessThan(p3));
        assertFalse(p3.isLessThan(p1));
        assertFalse(p1.isLessThan(p1));
    }
}

// Copyright (c) Naked Objects Group Ltd.
