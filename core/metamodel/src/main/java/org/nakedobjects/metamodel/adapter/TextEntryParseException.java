package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectApplicationException;


/**
 * Indicates that a text entry could not be satisfactorily parsed into a useful value by the value adapter.
 */
public class TextEntryParseException extends NakedObjectApplicationException {
    private static final long serialVersionUID = 1L;

    public TextEntryParseException() {
        super();
    }

    public TextEntryParseException(final String message) {
        super(message);
    }

    public TextEntryParseException(final Throwable cause) {
        super(cause);
    }

    public TextEntryParseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
// Copyright (c) Naked Objects Group Ltd.
