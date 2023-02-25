package org.nakedobjects.application.valueholder;

public class MultilineTextStringTests extends ValueTestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(MultilineTextStringTests.class);
    }

    public void testInvalidCharacters() {

        MultilineTextString t = new MultilineTextString("Hello\r");
        assertEquals("Hello\n", t.stringValue());

        t = new MultilineTextString("Hello\r\n");
        assertEquals("Hello\n", t.stringValue());

        t = new MultilineTextString("Hello\n\r");
        assertEquals("Hello\n\n", t.stringValue());
    }

    public void testTitle() {
        String text = "Hello\nYou";
        MultilineTextString t = new MultilineTextString(text);
        assertEquals("Hello\nYou", t.title().toString());

    }

    public void testValidCharacters() {
        String text = "Hello\tYou\n";
        MultilineTextString t = new MultilineTextString(text);
        assertEquals(text, t.stringValue());
    }
}
// Copyright (c) Naked Objects Group Ltd.
