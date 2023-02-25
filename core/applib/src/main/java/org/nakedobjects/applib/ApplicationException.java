package org.nakedobjects.applib;

/**
 * Indicates that a problem has occurred within the application, as opposed to within a supporting framework
 * or system.
 */
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Throwable cause;

    public ApplicationException(final String msg) {
        super(msg);
    }

    public ApplicationException(final Throwable cause) {
        this(cause.getMessage());
        this.cause = cause;
    }

    public ApplicationException(final String msg, final Throwable cause) {
        this(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
// Copyright (c) Naked Objects Group Ltd.
