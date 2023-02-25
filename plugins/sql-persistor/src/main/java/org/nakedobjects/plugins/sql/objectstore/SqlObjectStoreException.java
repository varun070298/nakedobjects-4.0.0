package org.nakedobjects.plugins.sql.objectstore;

import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class SqlObjectStoreException extends ObjectPersistenceException {
    private static final long serialVersionUID = 1L;

    public SqlObjectStoreException() {
        super();
    }

    public SqlObjectStoreException(final String s) {
        super(s);
    }

    public SqlObjectStoreException(final String s, final Throwable cause) {
        super(s, cause);
    }

    public SqlObjectStoreException(final Throwable cause) {
        super(cause);
    }
}
// Copyright (c) Naked Objects Group Ltd.
