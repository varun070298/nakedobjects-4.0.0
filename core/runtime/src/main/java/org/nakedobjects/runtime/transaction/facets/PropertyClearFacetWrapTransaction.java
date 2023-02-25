package org.nakedobjects.runtime.transaction.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetAbstract;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;


public class PropertyClearFacetWrapTransaction extends PropertyClearFacetAbstract implements DecoratingFacet<PropertyClearFacet> {

    private final PropertyClearFacet underlyingFacet;

    public PropertyClearFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public PropertyClearFacetWrapTransaction(final PropertyClearFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public void clearProperty(final NakedObject adapter) {
        if (adapter.isTransient()) {
        	// NOT !adapter.isPersistent();
        	// (value adapters are neither persistent or transient) 
            underlyingFacet.clearProperty(adapter);
        } else {
        	getTransactionManager().executeWithinTransaction(
    			new TransactionalClosureAbstract(){
    				public void execute() {
    					underlyingFacet.clearProperty(adapter);
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
