package org.nakedobjects.metamodel.commons.exceptions;



public class UnknownTypeException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public UnknownTypeException() {
        super();
    }

    public UnknownTypeException(final String message) {
        super(message);
    }

    public UnknownTypeException(final Throwable cause) {
        super(cause);
    }

    public UnknownTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnknownTypeException(final Object object) {
        this(object == null ? "null" : object.toString());
    }

}
// Copyright (c) Naked Objects Group Ltd.
