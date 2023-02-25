package org.nakedobjects.remoting.transport;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


/**
 * Indicates that a connection could not be established between the client and the server.
 */
public class ConnectionException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ConnectionException() {
        super();
    }

    public ConnectionException(final String msg) {
        super(msg);
    }

    public ConnectionException(final Throwable cause) {
        super(cause);
    }

    public ConnectionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
