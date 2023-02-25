package org.nakedobjects.runtime.transaction;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;

public class NakedObjectTransactionManagerException extends NakedObjectException {

    private static final long serialVersionUID = 1L;

    public NakedObjectTransactionManagerException() {}

    public NakedObjectTransactionManagerException(String message) {
        super(message);
    }

    public NakedObjectTransactionManagerException(Throwable cause) {
        super(cause);
    }

    public NakedObjectTransactionManagerException(String message, Throwable cause) {
        super(message, cause);
    }

}


// Copyright (c) Naked Objects Group Ltd.
