package org.nakedobjects.runtime.persistence;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class ObjectNotFoundException extends ObjectPersistenceException {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(final Oid oid) {
        super("Object not found in store with oid " + oid);
    }

    public ObjectNotFoundException(final Oid oid, Throwable cause) {
        super("Object not found in store with oid " + oid, cause);
    }

    public ObjectNotFoundException(final String s) {
        super(s);
    }
}
// Copyright (c) Naked Objects Group Ltd.
