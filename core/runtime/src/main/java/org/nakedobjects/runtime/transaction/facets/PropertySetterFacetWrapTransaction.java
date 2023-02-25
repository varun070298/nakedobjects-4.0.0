package org.nakedobjects.runtime.transaction.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetAbstract;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.TransactionalClosureAbstract;


public class PropertySetterFacetWrapTransaction extends PropertySetterFacetAbstract implements DecoratingFacet<PropertySetterFacet> {

    private final PropertySetterFacet underlyingFacet;

    public PropertySetterFacetWrapTransaction(final PropertySetterFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public PropertySetterFacet getDecoratedFacet() {
    	return underlyingFacet;
    }
    
    public void setProperty(final NakedObject adapter, final NakedObject referencedAdapter) {
        if (adapter.isTransient()) {
        	// NOT !adapter.isPersistent();
        	// (value adapters are neither persistent or transient) 
            underlyingFacet.setProperty(adapter, referencedAdapter);
        } else {
        	getTransactionManager().executeWithinTransaction(
    			new TransactionalClosureAbstract(){
    				public void execute() {
    					underlyingFacet.setProperty(adapter, referencedAdapter);
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
