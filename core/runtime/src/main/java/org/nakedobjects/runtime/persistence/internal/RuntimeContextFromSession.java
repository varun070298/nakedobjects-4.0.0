package org.nakedobjects.runtime.persistence.internal;

import java.util.List;

import org.nakedobjects.applib.ApplicationException;
import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAbstract;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerObjectChanged;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerResolve;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

/**
 * Provides services to the metamodel based on the currently running
 * {@link NakedObjectSession session} (primarily the {@link PersistenceSession}).
 */
public class RuntimeContextFromSession extends RuntimeContextAbstract {


    ////////////////////////////////////////////////////////////////////
	// AuthenticationSession
    ////////////////////////////////////////////////////////////////////

	public AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}

    ////////////////////////////////////////////////////////////////////
	// getAdapterFor, adapterFor
    ////////////////////////////////////////////////////////////////////

	public NakedObject getAdapterFor(Object pojo) {
		return getAdapterManager().getAdapterFor(pojo);
	}

	public NakedObject getAdapterFor(Oid oid) {
		return getAdapterManager().getAdapterFor(oid);
	}

	public NakedObject adapterFor(Object pojo) {
		return getAdapterManager().adapterFor(pojo);
	}

	public NakedObject adapterFor(Object pojo, NakedObject ownerAdapter, Identified identified) {
		return getAdapterManager().adapterFor(pojo, ownerAdapter, identified);
	}


    ////////////////////////////////////////////////////////////////////
	// createTransientInstance, instantiate
    ////////////////////////////////////////////////////////////////////
	
	public NakedObject createTransientInstance(NakedObjectSpecification spec) {
        return getPersistenceSession().createInstance(spec);
	}

	public Object instantiate(Class<?> cls) throws ObjectInstantiationException {
		return getPersistenceSession().getObjectFactory().instantiate(cls);
	}

	
    ////////////////////////////////////////////////////////////////////
	// resolve, objectChanged
    ////////////////////////////////////////////////////////////////////

	public void resolve(Object parent) {
        new DomainObjectContainerResolve().resolve(parent);
	}

	public void resolve(Object parent, Object field) {
        new DomainObjectContainerResolve().resolve(parent, field);
	}

	public void objectChanged(NakedObject adapter) {
		getPersistenceSession().objectChanged(adapter);
	}

	public void objectChanged(Object object) {
        new DomainObjectContainerObjectChanged().objectChanged(object);
	}

    ////////////////////////////////////////////////////////////////////
	// makePersistent, remove
    ////////////////////////////////////////////////////////////////////

	public void makePersistent(NakedObject adapter) {
		getPersistenceSession().makePersistent(adapter);
	}

	public void remove(NakedObject adapter) {
        getUpdateNotifier().addDisposedObject(adapter);
        getPersistenceSession().destroyObject(adapter);
	}

	
    ////////////////////////////////////////////////////////////////////
	// flush, commit
    ////////////////////////////////////////////////////////////////////

	public boolean flush() {
        return getTransactionManager().flushTransaction();
	}

	public void commit() {
		getTransactionManager().endTransaction();
	}
	

    ////////////////////////////////////////////////////////////////////
	// allInstances, allMatching*, *MatchingQuery
    ////////////////////////////////////////////////////////////////////

	public <T> List<NakedObject> allMatchingQuery(Query<T> query) {
		NakedObject instances = getPersistenceSession().findInstances(query, QueryCardinality.MULTIPLE);
		return CollectionFacetUtils.convertToAdapterList(instances);
	}

	public <T> NakedObject firstMatchingQuery(Query<T> query) {
		NakedObject instances = getPersistenceSession().findInstances(query, QueryCardinality.SINGLE);
		List<NakedObject> list = CollectionFacetUtils.convertToAdapterList(instances);
		return list.size() > 0? list.get(0): null;
	}


    ////////////////////////////////////////////////////////////////////
    // info, warn, error messages
    ////////////////////////////////////////////////////////////////////

	public void informUser(String message) {
		getMessageBroker().addMessage(message);		
	}

	public void warnUser(String message) {
		getMessageBroker().addWarning(message);
	}
	
	public void raiseError(String message) {
		throw new ApplicationException(message);
	}


	/////////////////////////////////////////////
	// getServices, injectDependenciesInto
	/////////////////////////////////////////////
	
	public List<NakedObject> getServices() {
		return getPersistenceSession().getServices();
	}

	public void injectDependenciesInto(Object object) {
		getPersistenceSession().getServicesInjector().injectDependencies(object);
	}

	/////////////////////////////////////////////
	// Dependencies (from context)
	/////////////////////////////////////////////
	
	private static PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}

	private static AdapterManager getAdapterManager() {
		return getPersistenceSession().getAdapterManager();
	}

	private static UpdateNotifier getUpdateNotifier() {
		return NakedObjectsContext.getUpdateNotifier();
	}

    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

    private static MessageBroker getMessageBroker() {
        return NakedObjectsContext.getMessageBroker();
    }



}
