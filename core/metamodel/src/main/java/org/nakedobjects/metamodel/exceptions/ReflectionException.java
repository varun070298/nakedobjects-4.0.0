package org.nakedobjects.metamodel.exceptions;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ReflectionException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ReflectionException() {
        super();
    }

    public ReflectionException(final String message) {
        super(message);
    }

    public ReflectionException(final Throwable cause) {
        super(cause);
    }

    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
