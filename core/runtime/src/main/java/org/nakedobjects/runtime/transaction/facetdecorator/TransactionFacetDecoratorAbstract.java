package org.nakedobjects.runtime.transaction.facetdecorator;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;


public abstract class TransactionFacetDecoratorAbstract  extends FacetDecoratorAbstract implements TransactionFacetDecorator {

    private NakedObjectConfiguration configuration;

    public TransactionFacetDecoratorAbstract(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    protected NakedObjectConfiguration getConfiguration() {
        return configuration;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[] { 
                ActionInvocationFacet.class,
                PropertyClearFacet.class, 
                PropertySetterFacet.class,  
                CollectionAddToFacet.class, 
                CollectionRemoveFromFacet.class, 
                CollectionClearFacet.class 
            };
    }
}

// Copyright (c) Naked Objects Group Ltd.
