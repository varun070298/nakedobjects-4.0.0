package org.nakedobjects.runtime.authentication;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


/**
 * Indicates that there is no Authenticator available to authenticate a user based on this request.
 */
public class NoAuthenticatorException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public NoAuthenticatorException() {}

    public NoAuthenticatorException(final String msg) {
        super(msg);
    }

    public NoAuthenticatorException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public NoAuthenticatorException(final Throwable cause) {
        super(cause);
    }

}

// Copyright (c) Naked Objects Group Ltd.
