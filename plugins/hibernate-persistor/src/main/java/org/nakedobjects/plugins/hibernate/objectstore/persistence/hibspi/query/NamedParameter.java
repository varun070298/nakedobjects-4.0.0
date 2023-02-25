package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query;

import java.io.Serializable;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.type.Type;

class NamedParameter implements Parameter, Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private transient Object value;
    private final Type type;

    public NamedParameter(final String name, final Object value, final Type type) {
        super();
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public void setParameterInto(final Query query) {
        if (type.equals(Hibernate.OBJECT)) {
            query.setEntity(name, value);
        } else if (type.equals(QueryPlaceholder.DETERMINE)) {
            query.setParameter(name, value);
        } else {
            query.setParameter(name, value, type);
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
