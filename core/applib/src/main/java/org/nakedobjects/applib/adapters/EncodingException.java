package org.nakedobjects.applib.adapters;

/**
 * Indicates that encoding or decoding has failed.
 */
public class EncodingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EncodingException() {
        super();
    }

    public EncodingException(final String msg) {
        super(msg);
    }

    public EncodingException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public EncodingException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
