package org.nakedobjects.runtime.persistence.query;

import java.io.IOException;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.services.container.query.QueryBuiltInAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

/**
 * Corresponds to {@link QueryBuiltInAbstract}.
 */
public abstract class PersistenceQueryBuiltInAbstract extends PersistenceQueryAbstract implements PersistenceQueryBuiltIn {

	public PersistenceQueryBuiltInAbstract(
			NakedObjectSpecification specification) {
		super(specification);
	}

	public PersistenceQueryBuiltInAbstract(DataInputExtended input)
			throws IOException {
		super(input);
	}

	
}

// Copyright (c) Naked Objects Group Ltd.
