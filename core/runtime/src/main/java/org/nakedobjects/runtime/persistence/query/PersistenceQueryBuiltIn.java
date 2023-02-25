package org.nakedobjects.runtime.persistence.query;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.metamodel.adapter.NakedObject;

public interface PersistenceQueryBuiltIn extends PersistenceQuery {

	/**
	 * The built-in queries iterate over all instances.
	 * 
	 * <p>
	 * This is similar to the {@link Filter} interface in the applib, except the
	 * filtering is done within the object store as opposed to be the
	 * {@link DomainObjectContainer}.
	 * 
	 * <p>
	 * Object store implementations do not necessarily need to rely on this
	 * method. For example, an RDBMS-based implementation may use an alternative
	 * mechanism to determine the matching results, for example using a
	 * <tt>WHERE</tt> clause in some SQL query.
	 * 
	 */
	public boolean matches(final NakedObject object);
}
// Copyright (c) Naked Objects Group Ltd.
