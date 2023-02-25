package org.nakedobjects.metamodel.commons.factory;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class InstanceCreationClassException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public InstanceCreationClassException() {
        super();
    }

    public InstanceCreationClassException(final String s) {
        super(s);
    }

    public InstanceCreationClassException(final Throwable cause) {
        super(cause);
    }

    public InstanceCreationClassException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
