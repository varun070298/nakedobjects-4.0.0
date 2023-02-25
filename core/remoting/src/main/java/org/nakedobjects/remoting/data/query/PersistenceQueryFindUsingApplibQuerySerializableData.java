package org.nakedobjects.remoting.data.query;

import java.io.Serializable;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQuerySerializable;

/**
 * Serializable representation of {@link PersistenceQueryFindUsingApplibQuerySerializable}.
 */
public class PersistenceQueryFindUsingApplibQuerySerializableData extends PersistenceQueryDataAbstract {
	
    private static final long serialVersionUID = 1L;
    private final Serializable querySerializable;
	private final QueryCardinality cardinality;
	
    public PersistenceQueryFindUsingApplibQuerySerializableData(
    		final NakedObjectSpecification noSpec, 
    		final Query applibQuery, QueryCardinality cardinality) {
        super(noSpec);
        querySerializable = applibQuery;
        this.cardinality = cardinality;
    }

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindUsingApplibQuerySerializable.class;
    }

    public Serializable getApplibQuerySerializable() {
		return querySerializable;
	}

	public QueryCardinality getCardinality() {
		return cardinality;
	}

}

// Copyright (c) Naked Objects Group Ltd.
