package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.data.query.PersistenceQueryFindUsingApplibQuerySerializableData;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindUsingApplibQuerySerializable;

public class PersistenceQueryFindUsingApplibQuerySerializableEncoder extends PersistenceQueryEncoderAbstract {

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByTitle.class;
    }
    
    public PersistenceQueryData encode(
    		final PersistenceQuery persistenceQuery) {
        PersistenceQueryFindUsingApplibQuerySerializable query = 
        	downcast(persistenceQuery);
		return new PersistenceQueryFindUsingApplibQuerySerializableData(
        		query.getSpecification(), 
        		query.getApplibQuery(), 
        		query.getCardinality());
    }

    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData persistenceQueryData) {
        PersistenceQueryFindUsingApplibQuerySerializableData data = downcast(persistenceQueryData);
		final Query query = (Query) data.getApplibQuerySerializable();
		final QueryCardinality cardinality = (QueryCardinality) data.getCardinality();
        return new PersistenceQueryFindUsingApplibQuerySerializable(specification, query, cardinality);
    }

	private PersistenceQueryFindUsingApplibQuerySerializable downcast(
			final PersistenceQuery persistenceQuery) {
		return (PersistenceQueryFindUsingApplibQuerySerializable) persistenceQuery;
	}

	private PersistenceQueryFindUsingApplibQuerySerializableData downcast(
			final PersistenceQueryData persistenceQueryData) {
		return (PersistenceQueryFindUsingApplibQuerySerializableData) persistenceQueryData;
	}

}

// Copyright (c) Naked Objects Group Ltd.
