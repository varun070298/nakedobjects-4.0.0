package org.nakedobjects.metamodel.commons.factory;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class UnavailableClassException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public UnavailableClassException() {
        super();
    }

    public UnavailableClassException(final String s) {
        super(s);
    }

    public UnavailableClassException(final Throwable cause) {
        super(cause);
    }

    public UnavailableClassException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
