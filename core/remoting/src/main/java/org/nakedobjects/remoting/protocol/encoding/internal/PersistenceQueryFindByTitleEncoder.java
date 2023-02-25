package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.data.query.PersistenceQueryFindByTitleData;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;

public class PersistenceQueryFindByTitleEncoder extends PersistenceQueryEncoderAbstract {

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByTitle.class;
    }

    public PersistenceQueryData encode(
    		final PersistenceQuery persistenceQuery) {
        PersistenceQueryFindByTitle queryByTitle = downcast(persistenceQuery);
		return new PersistenceQueryFindByTitleData(
        		persistenceQuery.getSpecification(), 
        		queryByTitle.getTitle());
    }

    @Override
    protected PersistenceQuery doDecode(
            final NakedObjectSpecification specification,
            final PersistenceQueryData persistenceQueryData) {
        final String title = (downcast(persistenceQueryData)).getTitle();
        return new PersistenceQueryFindByTitle(specification, title);
    }

	private PersistenceQueryFindByTitle downcast(
			final PersistenceQuery persistenceQuery) {
		return (PersistenceQueryFindByTitle) persistenceQuery;
	}

	private PersistenceQueryFindByTitleData downcast(
			final PersistenceQueryData persistenceQueryData) {
		return (PersistenceQueryFindByTitleData) persistenceQueryData;
	}

}

// Copyright (c) Naked Objects Group Ltd.
