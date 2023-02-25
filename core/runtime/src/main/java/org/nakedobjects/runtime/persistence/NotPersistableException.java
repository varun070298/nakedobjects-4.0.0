package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class NotPersistableException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public NotPersistableException() {
        super();
    }

    public NotPersistableException(final String message) {
        super(message);
    }

    public NotPersistableException(final Throwable cause) {
        super(cause);
    }

    public NotPersistableException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
