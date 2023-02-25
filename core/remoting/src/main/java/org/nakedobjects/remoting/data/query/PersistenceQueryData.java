package org.nakedobjects.remoting.data.query;

import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

/**
 * Serializable representation of {@link PersistenceQuery}.
 */
public interface PersistenceQueryData extends Data {
	Class<?> getPersistenceQueryClass();
}

// Copyright (c) Naked Objects Group Ltd.
