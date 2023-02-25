package org.nakedobjects.metamodel.services.container.query;

import java.io.Serializable;
import java.text.MessageFormat;

import org.nakedobjects.applib.query.Query;


/**
 * Although implements {@link Query} and thus is intended to be (and indeed is) {@link Serializable},
 * it will be converted into a <tt>PersistenceQuery</tt> in the runtime for remoting purposes.
 * 
 * <p>
 * See discussion in {@link QueryBuiltInAbstract} for further details.
 */
public class QueryFindByTitle<T> extends QueryBuiltInAbstract<T> {

	private static final long serialVersionUID = 1L;
	
	private final String title;
	
	public QueryFindByTitle(final Class<T> type, final String title) {
		super(type);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return MessageFormat.format("{0} (matching title: '{1}')", getResultTypeName(), getTitle());
	}

}
