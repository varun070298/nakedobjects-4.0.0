package org.nakedobjects.runtime.persistence.query;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.services.container.query.QueryFindAllInstances;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Corresponds to {@link QueryFindAllInstances}
 */
public class PersistenceQueryFindAllInstances extends PersistenceQueryBuiltInAbstract {
    public PersistenceQueryFindAllInstances(final NakedObjectSpecification specification) {
        super(specification);
    }

    /**
     * Returns true so it matches all instances.
     */
    public boolean matches(final NakedObject object) {
        return true;
    }

    @Override
    public String toString() {
        final ToString str = ToString.createAnonymous(this);
        str.append("spec", getSpecification().getShortName());
        return str.toString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
