package org.nakedobjects.runtime.system;

public class NakedObjectSystemException extends Exception {

    private static final long serialVersionUID = 1L;

    public NakedObjectSystemException() {}

    public NakedObjectSystemException(String message) {
        super(message);
    }

    public NakedObjectSystemException(Throwable cause) {
        super(cause);
    }

    public NakedObjectSystemException(String message, Throwable cause) {
        super(message, cause);
    }

}


// Copyright (c) Naked Objects Group Ltd.
