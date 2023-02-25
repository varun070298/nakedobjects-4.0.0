package org.nakedobjects.example.objectstore;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class DatabaseException extends NakedObjectException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(Exception e) {
        super(e);
    }

    public DatabaseException(String msg) {
        super(msg);
    }

}

// Copyright (c) Naked Objects Group Ltd.
