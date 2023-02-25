package org.nakedobjects.remoting.data.query;

import java.util.Map;

import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQueryDefault;

/**
 * Serializable representation of {@link PersistenceQueryFindUsingApplibQueryDefault}.
 */
public class PersistenceQueryFindUsingApplibQueryDefaultData extends PersistenceQueryDataAbstract {
	
    private static final long serialVersionUID = 1L;
    private final Map<String, ObjectData> argumentDatasByParameterName;
	private final QueryCardinality cardinality;
	private final String queryName;
	
    public PersistenceQueryFindUsingApplibQueryDefaultData(
    		final NakedObjectSpecification noSpec, 
    		final String queryName,
    		final Map<String, ObjectData> argumentDatasByParameterName, 
    		final QueryCardinality cardinality) {
        super(noSpec);
        this.queryName = queryName;
        this.argumentDatasByParameterName = argumentDatasByParameterName;
        this.cardinality = cardinality;
    }

	public String getQueryName() {
		return queryName;
	}
    
	public Map<String, ObjectData> getArgumentDatasByParameterName() {
		return argumentDatasByParameterName;
	}
	
	public QueryCardinality getCardinality() {
		return cardinality;
	}

	public Class<?> getPersistenceQueryClass() {
		return PersistenceQueryFindUsingApplibQueryDefault.class;
	}
	
}

// Copyright (c) Naked Objects Group Ltd.
