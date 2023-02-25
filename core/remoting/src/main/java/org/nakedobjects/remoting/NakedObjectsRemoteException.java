package org.nakedobjects.remoting;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


/**
 * Denotes an exception that occurred on the server.
 */
public class NakedObjectsRemoteException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public NakedObjectsRemoteException(final String msg) {
        super(msg);
    }

    public NakedObjectsRemoteException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public NakedObjectsRemoteException(final Throwable cause) {
        super(cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
