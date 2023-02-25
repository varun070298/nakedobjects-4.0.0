package org.nakedobjects.metamodel.commons.ensure;

public class Assert {

    public static void assertEquals(final Object expected, final Object actual) {
        assertEquals("", expected, actual);
    }

    public static void assertEquals(final String message, final int expected, final int value) {
        if (expected != value) {
            throw new NakedObjectAssertException(message + " expected " + expected + "; but was " + value);
        }
    }

    public static void assertEquals(final String message, final Object expected, final Object actual) {
        assertTrue(message + ": expected " + expected + " but was " + actual, (expected == null && actual == null)
                || (expected != null && expected.equals(actual)));
    }

    public static void assertFalse(final boolean flag) {
        assertFalse("expected false", flag);
    }

    public static void assertFalse(final String message, final boolean flag) {
        assertTrue(message, !flag);
    }

    public static void assertFalse(final String message, final Object target, final boolean flag) {
        assertTrue(message, target, !flag);
    }

    public static void assertNotNull(final Object object) {
        assertNotNull("", object);
    }

    public static void assertNotNull(final String message, final Object object) {
        assertTrue("unexpected null: " + message, object != null);
    }

    public static void assertNotNull(final String message, final Object target, final Object object) {
        assertTrue(message, target, object != null);
    }

    public static void assertNull(final Object object) {
        assertTrue("unexpected reference; should be null", object == null);
    }

    public static void assertNull(final String message, final Object object) {
        assertTrue(message, object == null);
    }

    public static void assertSame(final Object expected, final Object actual) {
        assertSame("", expected, actual);
    }

    public static void assertSame(final String message, final Object expected, final Object actual) {
        assertTrue(message + ": expected " + expected + " but was " + actual, expected == actual);
    }

    public static void assertTrue(final boolean flag) {
        assertTrue("expected true", flag);
    }

    public static void assertTrue(final String message, final boolean flag) {
        assertTrue(message, null, flag);
    }

    public static void assertTrue(final String message, final Object target, final boolean flag) {
        if (!flag) {
            throw new NakedObjectAssertException(message + (target == null ? "" : (": " + target)));
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
