package org.nakedobjects.plugins.html.component.html;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class HtmlException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public HtmlException() {}

    public HtmlException(final String msg) {
        super(msg);
    }

    public HtmlException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public HtmlException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
