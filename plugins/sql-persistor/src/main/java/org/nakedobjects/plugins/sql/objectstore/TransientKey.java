package org.nakedobjects.plugins.sql.objectstore;

public class TransientKey implements PrimaryKey {
    private static final long serialVersionUID = 1L;
    private long transientId;

    public TransientKey(final long id) {
        transientId = id;
    }

    public String stringValue() {
        return "" + transientId;
    }
}
// Copyright (c) Naked Objects Group Ltd.
