package org.nakedobjects.runtime.transaction.facetdecorator.standard;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.runtime.transaction.facetdecorator.TransactionFacetDecoratorAbstract;
import org.nakedobjects.runtime.transaction.facets.ActionInvocationFacetWrapTransaction;
import org.nakedobjects.runtime.transaction.facets.CollectionAddToFacetWrapTransaction;
import org.nakedobjects.runtime.transaction.facets.CollectionClearFacetWrapTransaction;
import org.nakedobjects.runtime.transaction.facets.CollectionRemoveFromFacetWrapTransaction;
import org.nakedobjects.runtime.transaction.facets.PropertyClearFacetWrapTransaction;
import org.nakedobjects.runtime.transaction.facets.PropertySetterFacetWrapTransaction;


public class StandardTransactionFacetDecorator extends TransactionFacetDecoratorAbstract {

    public StandardTransactionFacetDecorator(NakedObjectConfiguration configuration) {
        super(configuration);
    }

    public Facet decorate(final Facet facet, FacetHolder requiredHolder) {
        final Class<? extends Facet> facetType = facet.facetType();
        if (facetType == ActionInvocationFacet.class) {
            ActionInvocationFacet decoratedFacet = (ActionInvocationFacet) facet;
            Facet decoratingFacet = new ActionInvocationFacetWrapTransaction(decoratedFacet);
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == CollectionAddToFacet.class) {
            CollectionAddToFacet decoratedFacet = (CollectionAddToFacet) facet;
            Facet decoratingFacet = new CollectionAddToFacetWrapTransaction(decoratedFacet);
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == CollectionClearFacet.class) {
            CollectionClearFacet decoratedFacet = (CollectionClearFacet) facet;
            Facet decoratingFacet = new CollectionClearFacetWrapTransaction(decoratedFacet);
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == CollectionRemoveFromFacet.class) {
            CollectionRemoveFromFacet decoratedFacet = (CollectionRemoveFromFacet) facet;
            Facet decoratingFacet = new CollectionRemoveFromFacetWrapTransaction(decoratedFacet);
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == PropertyClearFacet.class) {
            PropertyClearFacet decoratedFacet = (PropertyClearFacet) facet;
            Facet decoratingFacet = new PropertyClearFacetWrapTransaction(decoratedFacet);
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        if (facetType == PropertySetterFacet.class) {
            PropertySetterFacet decoratedFacet = (PropertySetterFacet) facet;
            Facet decoratingFacet = new PropertySetterFacetWrapTransaction(decoratedFacet);
			return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }

        return facet;
    }

}

// Copyright (c) Naked Objects Group Ltd.
