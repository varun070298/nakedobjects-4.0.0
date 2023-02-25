package org.nakedobjects.remoting.data.query;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public abstract class PersistenceQueryDataAbstract implements PersistenceQueryData {
    private static final long serialVersionUID = 1L;
    private final String specification;

    public PersistenceQueryDataAbstract(NakedObjectSpecification noSpec) {
        specification = noSpec.getFullName();
    }

    public String getType() {
        return specification;
    }
}

// Copyright (c) Naked Objects Group Ltd.
