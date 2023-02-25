package org.nakedobjects.webserver;

public class WebServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WebServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
