package org.nakedobjects.metamodel.services.container.query;

import java.io.Serializable;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.applib.query.QueryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

/**
 * Although (through this class) the subclasses implements {@link Query} and
 * thus are meant to be {@link Serializable}, this isn't actually required
 * of the built-in queries because they are all converted into corresponding
 *  <tt>PersistenceQuery</tt> in the runtime for remoting purposes.
 *  
 * <p>
 * The principle reason for this is to reduce the size of the API from the
 * {@link DomainObjectContainer} to {@link RuntimeContext}, as well as possibly
 * to the embedded viewer's <tt>EmbeddedContext</tt>.  It also means that the
 * requirements for writing an object store are more easily expressed: support
 * the three built-in queries, plus any others.
 * 
 * <p>
 * Note also that the {@link QueryFindByPattern} isn't actually serializable
 * (because it references an arbitrary pojo).
 */
public abstract class QueryBuiltInAbstract<T> extends QueryAbstract<T> {

	private static final long serialVersionUID = 1L;

	public QueryBuiltInAbstract(final Class<T> type) {
		super(type);
	}

	public QueryBuiltInAbstract(final String typeName) {
		super(typeName);
	}

	public QueryBuiltInAbstract(final NakedObjectSpecification noSpec) {
		super(noSpec.getFullName());
	}

}
