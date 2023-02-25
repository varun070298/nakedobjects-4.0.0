package org.nakedobjects.runtime.transaction.facets;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacetAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureWithReturnAbstract;


public class ActionInvocationFacetWrapTransaction extends ActionInvocationFacetAbstract implements
        DecoratingFacet<ActionInvocationFacet> {

    private final static Logger LOG = Logger.getLogger(ActionInvocationFacetWrapTransaction.class);

    private final ActionInvocationFacet underlyingFacet;

    public ActionInvocationFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public ActionInvocationFacetWrapTransaction(final ActionInvocationFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public NakedObject invoke(final NakedObject targetAdapter, final NakedObject[] parameterAdapters) {
    	return getTransactionManager().executeWithinTransaction(
			new TransactionalClosureWithReturnAbstract<NakedObject>(){
				public NakedObject execute() {
					return underlyingFacet.invoke(targetAdapter, parameterAdapters);
				}});
    }

    public NakedObjectSpecification getReturnType() {
        return underlyingFacet.getReturnType();
    }

    public NakedObjectSpecification getOnType() {
        return underlyingFacet.getOnType();
    }

    @Override
    public String toString() {
        return super.toString() + " --> " + underlyingFacet.toString();
    }
    
    
    /////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////////////
    
    private static NakedObjectTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }



}

// Copyright (c) Naked Objects Group Ltd.
