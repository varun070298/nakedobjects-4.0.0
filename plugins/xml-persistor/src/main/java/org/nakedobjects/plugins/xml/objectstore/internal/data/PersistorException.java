package org.nakedobjects.plugins.xml.objectstore.internal.data;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class PersistorException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public PersistorException() {
        super();
    }

    public PersistorException(final String message) {
        super(message);
    }

    public PersistorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PersistorException(final Throwable cause) {
        super(cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
