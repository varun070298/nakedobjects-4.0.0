package org.nakedobjects.metamodel.commons.debug;

import junit.framework.TestCase;


public class DebugStringTest extends TestCase {
    private DebugString str;

    @Override
    protected void setUp() throws Exception {
        str = new DebugString();
    }

    public void testLFNotAddedToEmptyString() {
        str.blankLine();
        assertEquals("", str.toString());
    }

    public void testBlankLineAfterFirstLineWithLineFeed() {
        str.appendln("fred");
        str.blankLine();
        assertEquals("fred\n\n", str.toString());
    }

    public void testBlankLineAfterFirstLine() {
        str.append("fred");
        str.blankLine();
        assertEquals("fred\n\n", str.toString());
    }

    public void testOnlyOneBlankLine() {
        str.append("fred");
        str.blankLine();
        str.blankLine();
        str.blankLine();
        assertEquals("fred\n\n", str.toString());
    }

    public void testOnlyOneBlankLine2() {
        str.appendln("fred");
        str.blankLine();
        str.blankLine();
        str.blankLine();
        assertEquals("fred\n\n", str.toString());
    }
}

// Copyright (c) Naked Objects Group Ltd.
