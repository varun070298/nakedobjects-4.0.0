package org.nakedobjects.metamodel.runtimecontext.noruntime;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.identifier.Identified;

public class RuntimeContextNoRuntime extends RuntimeContextAbstract {

	public RuntimeContextNoRuntime() {
	}
	

	/////////////////////////////////////////////
	// AuthenticationSession
	/////////////////////////////////////////////

	public AuthenticationSession getAuthenticationSession() {
		return new AuthenticationSessionNoRuntime();
	}

	/////////////////////////////////////////////
	// getAdapterFor, adapterFor
	/////////////////////////////////////////////

	public NakedObject getAdapterFor(Object pojo) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}
	
	public NakedObject getAdapterFor(Oid oid) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}
	
	public NakedObject adapterFor(Object pattern) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}
	
	public NakedObject adapterFor(Object pojo, NakedObject ownerAdapter,
			Identified identified) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	
	/////////////////////////////////////////////
	// createTransientInstance, instantiate
	/////////////////////////////////////////////

	public NakedObject createTransientInstance(NakedObjectSpecification spec) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	public Object instantiate(Class<?> cls) throws ObjectInstantiationException {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	
	/////////////////////////////////////////////
	// resolve, objectChanged
	/////////////////////////////////////////////

	public void resolve(Object parent) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	public void resolve(Object parent, Object field) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	public void objectChanged(NakedObject inObject) {
		throw new UnsupportedOperationException(
				"Not supported by this implementation of RuntimeContext");
	}

	public void objectChanged(Object object) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	
	/////////////////////////////////////////////
	// makePersistent, remove
	/////////////////////////////////////////////
	
	public void makePersistent(NakedObject adapter) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	public void remove(NakedObject adapter) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	/////////////////////////////////////////////
	// flush, commit
	/////////////////////////////////////////////
	
	public boolean flush() {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	public void commit() {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	
	/////////////////////////////////////////////
	// allInstances, allMatching*
	/////////////////////////////////////////////

	public List<NakedObject> allInstances(NakedObjectSpecification noSpec) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	public <T> List<NakedObject> allMatchingQuery(Query<T> query) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	public <T> NakedObject firstMatchingQuery(Query<T> query) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

    ////////////////////////////////////////////////////////////////////
    // info, warn, error messages
    ////////////////////////////////////////////////////////////////////


	public void informUser(String message) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	public void warnUser(String message) {
		throw new UnsupportedOperationException(
			"Not supported by this implementation of RuntimeContext");
	}

	public void raiseError(String message) {
		throw new UnsupportedOperationException(
		"Not supported by this implementation of RuntimeContext");
	}

	
	/////////////////////////////////////////////
	// getServices, injectDependenciesInto
	/////////////////////////////////////////////

	/**
	 * Just returns an empty array.
	 */
	public List<NakedObject> getServices() {
		return new ArrayList<NakedObject>();
	}


	/**
	 * Unlike most of the methods in this implementation, does nothing (because
	 * this will always be called, even in a no-runtime context).
	 */
	public void injectDependenciesInto(Object object) {
		// does nothing.
	}



}
