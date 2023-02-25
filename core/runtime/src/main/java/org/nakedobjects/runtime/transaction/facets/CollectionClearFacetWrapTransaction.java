package org.nakedobjects.runtime.transaction.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacetAbstract;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;


public class CollectionClearFacetWrapTransaction extends CollectionClearFacetAbstract implements
        DecoratingFacet<CollectionClearFacet> {

    private final CollectionClearFacet underlyingFacet;

    public CollectionClearFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public CollectionClearFacetWrapTransaction(final CollectionClearFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public void clear(final NakedObject adapter) {
        if (adapter.isTransient()) {
        	// NOT !adapter.isPersistent();
        	// (value adapters are neither persistent or transient) 
            underlyingFacet.clear(adapter);
        } else {
	    	getTransactionManager().executeWithinTransaction(
				new TransactionalClosureAbstract(){
					public void execute() {
						underlyingFacet.clear(adapter);
					}});
        }
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
