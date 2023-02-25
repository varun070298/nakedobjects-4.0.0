package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

/**
 * Base class for immutable Hibernate user and composite types.
 */
public abstract class ImmutableType {

    public Object deepCopy(final Object value) {
        return value;
    }

    public boolean equals(final Object x, final Object y) {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    public int hashCode(final Object x) {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
