package org.nakedobjects.plugins.sql.objectstore;

public class StringPrimaryKey implements PrimaryKey {
    private static final long serialVersionUID = 1L;
    private final String primaryKey;

    public StringPrimaryKey(final String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof StringPrimaryKey) {
            StringPrimaryKey o = ((StringPrimaryKey) obj);
            return primaryKey == o.primaryKey;
        }
        return false;
    }

    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + primaryKey.hashCode();
        return hash;
    }

    public String stringValue() {
        return "" + primaryKey;
    }

    public String toString() {
        return "" + primaryKey;
    }
}
// Copyright (c) Naked Objects Group Ltd.
