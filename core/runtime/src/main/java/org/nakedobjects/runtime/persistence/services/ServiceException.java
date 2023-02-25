package org.nakedobjects.runtime.persistence.services;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class ServiceException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
