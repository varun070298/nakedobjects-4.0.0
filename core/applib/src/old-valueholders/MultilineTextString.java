package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;


public class MultilineTextString extends TextString {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public MultilineTextString() {
        super();
    }

    /**
     * @param text
     */
    public MultilineTextString(final String text) {
        super(text);
    }

    /**
     * @param textString
     */
    public MultilineTextString(final MultilineTextString textString) {
        super(textString);
    }

    /**
     * 
     */
    public MultilineTextString(final BusinessObject parent) {
        super(parent);
    }

    /**
     * @param text
     */
    public MultilineTextString(final BusinessObject parent, final String text) {
        super(parent, text);
    }

    /**
     * @param textString
     */
    public MultilineTextString(final BusinessObject parent, final MultilineTextString textString) {
        super(parent, textString);
    }

    /*
     * @see org.nakedobjects.object.value.TextString#setValue(java.lang.String)
     */
    public void setValue(final String text) {
        super.setValue(convertLineEnding(text));
    }

    public void restoreFromEncodedString(final String data) {
        super.restoreFromEncodedString(convertLineEnding(data));
    }

    protected boolean isCharDisallowed(final char c) {
        return c == '\r';
    }

    private String convertLineEnding(final String original) {
        if (original == null)
            return null;
        /*
         * convert all line ending to LF e.g. CR -> LF CRLF -> LF
         */
        StringBuffer text = new StringBuffer(original.length());

        for (int i = 0; i < original.length(); i++) {
            if (original.charAt(i) == '\r') {
                text.append('\n');

                // skip next char if LF (ie is a CRLF sequence
                if (i + 1 < original.length() && original.charAt(i + 1) == '\n') {
                    i++;
                }
            } else {
                text.append(original.charAt(i));
            }
        }

        return text.toString();
    }

    /*
     * int numberOfLine() int maxWidth();
     * 
     */

}
// Copyright (c) Naked Objects Group Ltd.
