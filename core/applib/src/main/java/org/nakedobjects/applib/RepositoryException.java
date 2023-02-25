package org.nakedobjects.applib;

/**
 * Indicates that a repository method has failed.
 */
public class RepositoryException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Throwable cause;

    public RepositoryException() {
        super();
    }

    public RepositoryException(final String msg) {
        super(msg);
    }

    public RepositoryException(final Throwable cause) {
        this(cause.getMessage());
        this.cause = cause;
    }

    public RepositoryException(final String msg, final Throwable cause) {
        this(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
// Copyright (c) Naked Objects Group Ltd.
