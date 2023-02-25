package org.nakedobjects.metamodel.commons.exceptions;



/**
 * Indicates that a call was made to a method (normally an overridden one) that was not expected, and hence
 * not coded for.
 */
public class UnexpectedCallException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public UnexpectedCallException() {
        super("This method call was not expected");
    }

    public UnexpectedCallException(final String arg0) {
        super(arg0);
    }

}
// Copyright (c) Naked Objects Group Ltd.
