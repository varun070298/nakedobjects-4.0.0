package org.nakedobjects.runtime.transaction.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacetAbstract;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;


public class CollectionRemoveFromFacetWrapTransaction extends CollectionRemoveFromFacetAbstract implements
        DecoratingFacet<CollectionRemoveFromFacet> {

    private final CollectionRemoveFromFacet underlyingFacet;

    public CollectionRemoveFromFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public CollectionRemoveFromFacetWrapTransaction(final CollectionRemoveFromFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public void remove(final NakedObject adapter, final NakedObject referencedAdapter) {
        if (adapter.isTransient()) {
        	// NOT !adapter.isPersistent();
        	// (value adapters are neither persistent or transient) 
            underlyingFacet.remove(adapter, referencedAdapter);
        } else {
        	getTransactionManager().executeWithinTransaction(
    			new TransactionalClosureAbstract(){
    				public void execute() {
    					underlyingFacet.remove(adapter, referencedAdapter);
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
