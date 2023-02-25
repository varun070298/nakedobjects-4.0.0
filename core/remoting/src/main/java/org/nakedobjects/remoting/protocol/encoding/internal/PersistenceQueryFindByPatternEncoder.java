package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.data.query.PersistenceQueryFindByPatternData;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByPattern;

public class PersistenceQueryFindByPatternEncoder extends PersistenceQueryEncoderAbstract {

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByPattern.class;
    }
    
    public PersistenceQueryData encode(
    		final PersistenceQuery persistenceQuery) {
        final PersistenceQueryFindByPattern patternPersistenceQuery = downcast(persistenceQuery);
        final NakedObject pattern = patternPersistenceQuery.getPattern();
        final ObjectData objectData = encodeObject(pattern);
        return new PersistenceQueryFindByPatternData(patternPersistenceQuery.getSpecification(), objectData);
    }

    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData persistenceQueryData) {
        final ObjectData patternData = downcast(persistenceQueryData).getPatternData();
        final NakedObject patternObject = decodeObject(patternData);
        return new PersistenceQueryFindByPattern(specification, patternObject);
    }

	private PersistenceQueryFindByPattern downcast(
			final PersistenceQuery persistenceQuery) {
		return (PersistenceQueryFindByPattern) persistenceQuery;
	}

	private PersistenceQueryFindByPatternData downcast(
			final PersistenceQueryData persistenceQueryData) {
		return (PersistenceQueryFindByPatternData)persistenceQueryData;
	}


}

// Copyright (c) Naked Objects Group Ltd.
