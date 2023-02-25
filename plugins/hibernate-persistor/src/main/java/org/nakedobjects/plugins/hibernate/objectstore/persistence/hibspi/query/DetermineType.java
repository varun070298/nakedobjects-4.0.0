package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query;

import org.hibernate.type.AnyType;

class DetermineType extends AnyType {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof DetermineType);
    }
}

// Copyright (c) Naked Objects Group Ltd.
