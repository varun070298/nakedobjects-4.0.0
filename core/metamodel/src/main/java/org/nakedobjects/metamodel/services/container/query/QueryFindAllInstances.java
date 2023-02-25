package org.nakedobjects.metamodel.services.container.query;

import java.io.Serializable;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

/**
 * Although implements {@link Query} and thus is intended to be (and indeed is) {@link Serializable},
 * it will be converted into a <tt>PersistenceQuery</tt> in the runtime for remoting purposes.
 * 
 * <p>
 * See discussion in {@link QueryBuiltInAbstract} for further details.
 */
public class QueryFindAllInstances<T> extends QueryBuiltInAbstract<T> {

	private static final long serialVersionUID = 1L;
	
	public QueryFindAllInstances(final Class<T> type) {
		super(type);
	}

	public QueryFindAllInstances(final String typeName) {
		super(typeName);
	}

	public QueryFindAllInstances(final NakedObjectSpecification noSpec) {
		super(noSpec);
	}

	public String getDescription() {
		return getResultTypeName() + " (all instances)";
	}

}
