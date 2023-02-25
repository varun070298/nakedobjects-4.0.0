package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectApplicationException;


/**
 * Indicates that a value entry is not valid. Note the entry may still parse correctly, but is does fulfil
 * other other entry requirements.
 */
public class InvalidEntryException extends NakedObjectApplicationException {
    private static final long serialVersionUID = 1L;

    public InvalidEntryException(final String message) {
        super(message);
    }

    public InvalidEntryException(final Throwable cause) {
        this("Invalid value", cause);
    }

    public InvalidEntryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
