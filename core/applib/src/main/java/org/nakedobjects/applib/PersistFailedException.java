package org.nakedobjects.applib;

/**
 * Indicates that the persistence of an object failed.
 */
public class PersistFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Throwable cause;

    public PersistFailedException() {
        super();
    }

    public PersistFailedException(final String msg) {
        super(msg);
    }

    public PersistFailedException(final Throwable cause) {
        this(cause.getMessage());
        this.cause = cause;
    }

    public PersistFailedException(final String msg, final Throwable cause) {
        this(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
// Copyright (c) Naked Objects Group Ltd.
