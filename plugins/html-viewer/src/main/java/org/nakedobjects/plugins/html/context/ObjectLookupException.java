package org.nakedobjects.plugins.html.context;

import org.nakedobjects.plugins.html.action.ActionException;


public class ObjectLookupException extends ActionException {
    private static final long serialVersionUID = 1L;

    public ObjectLookupException() {}

    public ObjectLookupException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public ObjectLookupException(final String msg) {
        super(msg);
    }

    public ObjectLookupException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
