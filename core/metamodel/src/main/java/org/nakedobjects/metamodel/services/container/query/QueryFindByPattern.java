package org.nakedobjects.metamodel.services.container.query;

import java.io.Serializable;

import org.nakedobjects.applib.query.Query;


/**
 * Although implements {@link Query} and thus is ought to be {@link Serializable},
 * it will be converted into a <tt>PersistenceQuery</tt> in the runtime for remoting purposes
 * and so does not need to be (and indeed isn't). 
 * 
 * <p>
 * See discussion in {@link QueryBuiltInAbstract} for further details.
 */
public class QueryFindByPattern<T> extends QueryBuiltInAbstract<T> {

	private static final long serialVersionUID = 1L;
	
	private final T pattern;
	
	public QueryFindByPattern(final Class<T> type, T pattern) {
		super(type);
		this.pattern = pattern;
	}

	public T getPattern() {
		return pattern;
	}
	
	public String getDescription() {
		return getResultTypeName() + " (matching pattern)";
	}

}
