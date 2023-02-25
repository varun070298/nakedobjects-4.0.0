package org.nakedobjects.metamodel.spec;

/**
 * A runtime exception indicating an problem has occurred within the Naked Objects framework.
 */
public class NakedObjectSpecificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NakedObjectSpecificationException() {
        super();
    }

    public NakedObjectSpecificationException(final String s) {
        super(s);
    }
}
// Copyright (c) Naked Objects Group Ltd.
