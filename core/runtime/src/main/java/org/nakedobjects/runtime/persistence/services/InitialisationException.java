package org.nakedobjects.runtime.persistence.services;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


/**
 * Indicates a problem initialising the naked objects system.
 */
public class InitialisationException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public InitialisationException() {
        super();
    }

    public InitialisationException(final String s) {
        super(s);
    }

    public InitialisationException(final Throwable cause) {
        super(cause);
    }

    public InitialisationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
