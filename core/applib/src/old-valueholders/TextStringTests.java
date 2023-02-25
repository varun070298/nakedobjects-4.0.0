package org.nakedobjects.application.valueholder;

public class TextStringTests extends ValueTestCase {
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TextStringTests.class);
    }

    public void testInvalidCharacters() {
        try {
            TextString t = new TextString();
            t.setValue("Hello\nYou");
            fail("Exception expected");
        } catch (RuntimeException expected) {}
        try {
            new TextString("Hello\nYou");
            fail("Exception expected");
        } catch (RuntimeException expected) {}
    }

    public void testValidCharacters() {
        String text = "Hello You";
        TextString t = new TextString(text);
        assertEquals(text, t.title().toString());
    }
}
// Copyright (c) Naked Objects Group Ltd.
