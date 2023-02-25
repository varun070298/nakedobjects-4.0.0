package org.nakedobjects.metamodel.commons.factory;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class InstanceCreationException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public InstanceCreationException() {
        super();
    }

    public InstanceCreationException(final String s) {
        super(s);
    }

    public InstanceCreationException(final Throwable cause) {
        super(cause);
    }

    public InstanceCreationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
