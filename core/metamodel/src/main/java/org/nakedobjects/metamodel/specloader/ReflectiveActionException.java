package org.nakedobjects.metamodel.specloader;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ReflectiveActionException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ReflectiveActionException() {
        super();
    }

    public ReflectiveActionException(final String msg) {
        super(msg);
    }

    public ReflectiveActionException(final Throwable cause) {
        super(cause);
    }

    public ReflectiveActionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
