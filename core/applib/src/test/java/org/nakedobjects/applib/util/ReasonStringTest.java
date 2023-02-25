package org.nakedobjects.applib.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ReasonStringTest {
    private ReasonBuffer reason;

    @Before
    public void setUp() {
        reason = new ReasonBuffer();
    }

    @Test
    public void testNoReasonReturnsNull() throws Exception {
        assertEquals(null, reason.getReason());
    }

    @Test
    public void testAReasonReturnsString() throws Exception {
        reason.append("reason 1");
        assertEquals("reason 1", reason.getReason());
    }

    @Test
    public void testConditionalAppendWithTrue() {
        reason.appendOnCondition(true, "reason 1");
        assertEquals("reason 1", reason.getReason());
    }

    @Test
    public void testConditionalAppendWithFalse() {
        reason.appendOnCondition(false, "reason 1");
        assertEquals(null, reason.getReason());
    }

    @Test
    public void testAppendTwoReasons() {
        reason.append("reason 1");
        reason.append("reason 2");
        assertEquals("reason 1; reason 2", reason.getReason());

    }

}

// Copyright (c) Naked Objects Group Ltd.
