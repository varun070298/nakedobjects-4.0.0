package org.nakedobjects.runtime.persistence.query;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Defines a criteria for including instances in set, 
 * corresponds to {@link Query} in the applib.
 */
public interface PersistenceQuery {

    /**
     * The type of instances in the resulting set.
     */
    NakedObjectSpecification getSpecification();
}
// Copyright (c) Naked Objects Group Ltd.
