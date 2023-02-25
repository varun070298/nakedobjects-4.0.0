package org.nakedobjects.metamodel.commons.exceptions;



/**
 * Flags a method as not having been implemented yet, but is be implemented.
 */
public class NotYetImplementedException extends NakedObjectException {

    private static final long serialVersionUID = 1L;

    public NotYetImplementedException() {
        super("This method is not implemented yet");
    }

    public NotYetImplementedException(final String arg0) {
        super(arg0);
    }

}
// Copyright (c) Naked Objects Group Ltd.
