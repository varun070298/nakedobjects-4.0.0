package org.nakedobjects.metamodel.runtimecontext;

import java.util.List;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

/**
 * Decouples the metamodel from a runtime.
 * 
 */
public interface RuntimeContext extends Injectable {


	/////////////////////////////////////////////
	// SpecificationLoader
	/////////////////////////////////////////////

	public SpecificationLoader getSpecificationLoader();


	/////////////////////////////////////////////
	// AuthenticationSession
	/////////////////////////////////////////////

	/**
	 * Provided by <tt>AuthenticationManager</tt> when used by framework.
	 */
	AuthenticationSession getAuthenticationSession();

	
	
	/////////////////////////////////////////////
	// getAdapterFor, adapterFor
	/////////////////////////////////////////////

	/**
	 * Provided by the <tt>AdapterManager</tt> when used by framework.
	 */
	NakedObject getAdapterFor(Oid oid);

	/**
	 * Provided by the <tt>AdapterManager</tt> when used by framework.
	 */
	NakedObject getAdapterFor(Object domainObject);

	/**
	 * Provided by the <tt>AdapterManager</tt> when used by framework.
	 */
	NakedObject adapterFor(Object domainObject);

	/**
	 * Provided by the <tt>AdapterManager</tt> when used by framework.
	 */
	NakedObject adapterFor(Object domainObject, NakedObject ownerAdapter, Identified identified);


	/////////////////////////////////////////////
	// createTransientInstance, instantiate
	/////////////////////////////////////////////
	
	/**
	 * Provided by the <tt>PersistenceSession</tt> when used by framework.
	 */
	NakedObject createTransientInstance(NakedObjectSpecification spec);
	
	/**
	 * Provided by the <tt>ObjectFactory</tt> when used by framework.
	 */
	Object instantiate(Class<?> cls) throws ObjectInstantiationException;
	

	/////////////////////////////////////////////
	// resolve, objectChanged
	/////////////////////////////////////////////

	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	void resolve(Object parent);

	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	void resolve(Object parent, Object field);

	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	void objectChanged(NakedObject adapter);

	/**
	 * TODO: combined with {@link #objectChanged(NakedObject)}.
	 */
	void objectChanged(Object object);

	
	/////////////////////////////////////////////
	// makePersistent, remove
	/////////////////////////////////////////////

	/**
	 * Provided by the <tt>PersistenceSession</tt> when used by framework.
	 */
	void makePersistent(NakedObject adapter);

	/**
	 * Provided by <tt>UpdateNotifier</tt> and <tt>PersistenceSession</tt>
	 * when used by framework.
	 */
	void remove(NakedObject adapter);


	/////////////////////////////////////////////
	// flush, commit
	/////////////////////////////////////////////

	/**
	 * Provided by <tt>TransactionManager</tt> when used by framework.
	 */
	boolean flush();

	/**
	 * Provided by <tt>TransactionManager</tt> when used by framework.
	 */
	void commit();



	/////////////////////////////////////////////
	// *MatchingQuery
	/////////////////////////////////////////////

	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	public <T> List<NakedObject> allMatchingQuery(Query<T> query);


	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	public <T> NakedObject firstMatchingQuery(Query<T> query);
	

    ////////////////////////////////////////////////////////////////////
    // info, warn, error messages
    ////////////////////////////////////////////////////////////////////
	
	/**
	 * Provided by <tt>MessageBroker</tt> when used by framework.
	 */
	void informUser(String message);

	/**
	 * Provided by <tt>MessageBroker</tt> when used by framework.
	 */
	void warnUser(String message);

	void raiseError(String message);

	
	
	/////////////////////////////////////////////
	// getServices, injectDependenciesInto
	/////////////////////////////////////////////

	/**
	 * Provided by <tt>PersistenceSession</tt> when used by framework.
	 */
	List<NakedObject> getServices();

	/**
	 * Provided by the <tt>ServicesInjectorDefault</tt> when used by framework.
	 */
	void injectDependenciesInto(final Object domainObject);


	public void setContainer(DomainObjectContainer container);






}
