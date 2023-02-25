package org.nakedobjects.applib.value;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String number;

    public PhoneNumber(final String text) {
        this.number = text;
    }

    public Object getValue() {
        return number;
    }

    public String toString() {
        return number;
    }
}
// Copyright (c) Naked Objects Group Ltd.
