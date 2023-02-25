package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.query;

import org.hibernate.Query;

public interface Parameter {
    public void setParameterInto(final Query query);

    public Object getValue();

    public void setValue(Object value);
}

// Copyright (c) Naked Objects Group Ltd.
