package org.nakedobjects.remoting.data.query;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;

/**
 * Serializable representation of {@link PersistenceQueryFindAllInstances}.
 */
public class PersistenceQueryFindAllInstancesData extends PersistenceQueryDataAbstract {
    private static final long serialVersionUID = 1L;

    public PersistenceQueryFindAllInstancesData(final NakedObjectSpecification nakedObjectSpecification) {
        super(nakedObjectSpecification);
    }

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindAllInstances.class;
    }
}

// Copyright (c) Naked Objects Group Ltd.
