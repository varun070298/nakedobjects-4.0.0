package org.nakedobjects.applib.query;

import java.io.Serializable;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.Filter;

/**
 * For use by repository implementations, representing the values of a query.
 * 
 * <p>
 * The implementations of these objects are be provided by the underlying
 * persistor/object store; consult its documentation.
 * 
 * <p>
 * <b>Note:</b> that not every object store will necessarily support this
 * interface. In particular, the in-memory object store does not. For this, you
 * can use the {@link Filter} interface to similar effect, for example in
 * {@link DomainObjectContainer#allMatches(Class, Filter)}). Note that the
 * filtering is done within the {@link DomainObjectContainer} rather than being
 * pushed back to the object store.
 */
public interface Query<T> extends Serializable {

	/**
	 * The {@link Class} of the objects returned by this query.
	 */
	public Class<T> getResultType();

	/**
	 * A human-readable representation of this query and its values.
	 */
	public String getDescription();
}
