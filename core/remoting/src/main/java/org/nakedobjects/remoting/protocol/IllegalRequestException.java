package org.nakedobjects.remoting.protocol;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


/**
 * Thrown when a request is received from a client who is not authorised to make it.
 */
public class IllegalRequestException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public IllegalRequestException(final String msg) {
        super(msg);
    }

    public IllegalRequestException(final Throwable cause) {
        super(cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
