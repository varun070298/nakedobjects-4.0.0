package org.nakedobjects.metamodel.commons.exceptions;

/**
 * Indicates an error raised by the application code.
 */
public class NakedObjectApplicationException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public NakedObjectApplicationException() {
        super();
    }

    public NakedObjectApplicationException(final String msg) {
        super(msg);
    }

    public NakedObjectApplicationException(final Throwable cause) {
        super(cause);
    }

    public NakedObjectApplicationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
