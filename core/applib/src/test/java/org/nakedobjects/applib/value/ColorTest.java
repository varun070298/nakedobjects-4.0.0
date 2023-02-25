package org.nakedobjects.applib.value;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ColorTest {
    @Test
    public void testToTitle() throws Exception {
        assertEquals("Black", Color.BLACK.title());
        assertEquals("White", Color.WHITE.title());
        assertEquals("#FE231D", new Color(0xfe231D).title());
    }

    @Test
    public void testIsLessThan() throws Exception {
        assertTrue(Color.BLACK.isLessThan(Color.WHITE));
    }
}

// Copyright (c) Naked Objects Group Ltd.
