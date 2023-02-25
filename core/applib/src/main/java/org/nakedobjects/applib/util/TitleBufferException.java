package org.nakedobjects.applib.util;

public class TitleBufferException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Throwable cause;

    public TitleBufferException() {
        super();
    }

    public TitleBufferException(final String msg) {
        super(msg);
    }

    public TitleBufferException(final Throwable cause) {
        this(cause.getMessage());
        this.cause = cause;
    }

    public TitleBufferException(final String msg, final Throwable cause) {
        this(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}


// Copyright (c) Naked Objects Group Ltd.
