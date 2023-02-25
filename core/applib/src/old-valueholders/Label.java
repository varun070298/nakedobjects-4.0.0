package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;


/**
 * A read-only text string. This class does support value listeners.
 */
public class Label extends TextString {
    private static final long serialVersionUID = 1L;

    public Label() {
        super();
    }

    public Label(final String text) {
        super(text);
    }

    public Label(final BusinessObject parent) {
        super(parent);
    }

    public Label(final BusinessObject parent, final String text) {
        super(parent, text);
    }

    public boolean userChangeable() {
        return false;
    }

    public String getObjectHelpText() {
        return "A Label object.";
    }
}
// Copyright (c) Naked Objects Group Ltd.
