package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class UnsupportedFindException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public UnsupportedFindException() {
        super();
    }

    public UnsupportedFindException(final String message) {
        super(message);
    }

    public UnsupportedFindException(final Throwable cause) {
        super(cause);
    }

    public UnsupportedFindException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
