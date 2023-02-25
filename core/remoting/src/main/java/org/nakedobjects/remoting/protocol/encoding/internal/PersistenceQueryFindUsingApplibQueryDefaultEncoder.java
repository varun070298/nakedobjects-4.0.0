package org.nakedobjects.remoting.protocol.encoding.internal;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.data.query.PersistenceQueryFindUsingApplibQueryDefaultData;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQueryDefault;

public class PersistenceQueryFindUsingApplibQueryDefaultEncoder extends PersistenceQueryEncoderAbstract {

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByTitle.class;
    }
    
    public PersistenceQueryData encode(
    		final PersistenceQuery persistenceQuery) {
        PersistenceQueryFindUsingApplibQueryDefault findByQuery = downcast(persistenceQuery);
		return new PersistenceQueryFindUsingApplibQueryDefaultData(
        		findByQuery.getSpecification(), 
        		findByQuery.getQueryName(),
        		encode(findByQuery.getArgumentsAdaptersByParameterName()),
        		findByQuery.getCardinality());
    }
    
    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData persistenceQueryData) {
        PersistenceQueryFindUsingApplibQueryDefaultData findByQueryData = downcast(persistenceQueryData);
		return new PersistenceQueryFindUsingApplibQueryDefault(
        		specification,
        		findByQueryData.getQueryName(), 
        		decode(findByQueryData.getArgumentDatasByParameterName()), 
        		((QueryCardinality) findByQueryData.getCardinality()));
    }

    
    private Map<String, ObjectData> encode(
			Map<String, NakedObject> argumentsAdaptersByParameterName) {
    	Map<String, ObjectData> argumentDatasByParameterName = new HashMap<String, ObjectData>();
		for(String parameterName: argumentsAdaptersByParameterName.keySet()) {
			NakedObject adapter = argumentsAdaptersByParameterName.get(parameterName);
			argumentDatasByParameterName.put(parameterName, encodeObject(adapter));
		}
		return argumentDatasByParameterName;
	}

	private Map<String, NakedObject> decode(
			Map<String, ObjectData> argumentDatasByParameterName) {
    	Map<String, NakedObject> argumentAdaptersByParameterName = new HashMap<String, NakedObject>();
		for(String parameterName: argumentDatasByParameterName.keySet()) {
			ObjectData data = argumentDatasByParameterName.get(parameterName);
			argumentAdaptersByParameterName.put(parameterName, decodeObject(data));
		}
		return argumentAdaptersByParameterName;
	}

	
	private PersistenceQueryFindUsingApplibQueryDefault downcast(
			final PersistenceQuery persistenceQuery) {
		return (PersistenceQueryFindUsingApplibQueryDefault) persistenceQuery;
	}

	private PersistenceQueryFindUsingApplibQueryDefaultData downcast(
			final PersistenceQueryData persistenceQueryData) {
		return (PersistenceQueryFindUsingApplibQueryDefaultData) persistenceQueryData;
	}

}

// Copyright (c) Naked Objects Group Ltd.
