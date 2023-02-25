package org.nakedobjects.metamodel.exceptions;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;

/**
 * Thrown when a problem is found with the domain model, and Naked Objects cannot proceed.
 */
public class ModelException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public ModelException() {
        super();
    }

    public ModelException(final String message) {
        super(message);
    }

    public ModelException(final Throwable cause) {
        super(cause);
    }

    public ModelException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
