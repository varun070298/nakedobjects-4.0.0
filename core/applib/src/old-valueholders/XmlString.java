package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.BusinessObject;


public class XmlString extends MultilineTextString {

    public XmlString() {
        this(null);
    }

    public XmlString(final BusinessObject parent) {
        super(parent);
    }

    /**
     * Determines if the user can change this type of object: no in the case of XmlValues.
     */
    public boolean userChangeable() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
