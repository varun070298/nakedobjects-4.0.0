package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query;

import java.io.Serializable;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.type.Type;

class IndexedParameter implements Parameter, Serializable {
    private static final long serialVersionUID = 1L;
    private final int index;
    private transient Object value;
    private final Type type;

    public IndexedParameter(final int index, final Object value, final Type type) {
        super();
        this.index = index;
        this.value = value;
        this.type = type;
    }

    public void setParameterInto(final Query query) {
        if (type.equals(Hibernate.OBJECT)) {
            query.setEntity(index, value);
        } else if (type.equals(QueryPlaceholder.DETERMINE)) {
            query.setParameter(index, value);
        } else {
            query.setParameter(index, value, type);
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

}

// Copyright (c) Naked Objects Group Ltd.
