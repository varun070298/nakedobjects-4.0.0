package org.nakedobjects.plugins.html.action;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ActionException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ActionException() {
        super();
    }

    public ActionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public ActionException(final String msg) {
        super(msg);
    }

    public ActionException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
