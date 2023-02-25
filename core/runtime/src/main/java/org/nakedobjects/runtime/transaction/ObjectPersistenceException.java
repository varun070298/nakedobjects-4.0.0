package org.nakedobjects.runtime.transaction;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ObjectPersistenceException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ObjectPersistenceException() {
        super();
    }

    public ObjectPersistenceException(final String message) {
        super(message);
    }

    public ObjectPersistenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectPersistenceException(final Throwable cause) {
        super(cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
