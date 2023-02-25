package org.nakedobjects.metamodel.facets.collections.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.CollectionAddToContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class CollectionValidateAddToFacetAbstract extends FacetAbstract implements CollectionValidateAddToFacet {

    public static Class<? extends Facet> type() {
        return CollectionValidateAddToFacet.class;
    }

    public CollectionValidateAddToFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof CollectionAddToContext)) {
            return null;
        }
        final CollectionAddToContext collectionAddToContext = (CollectionAddToContext) context;
        return invalidReason(context.getTarget(), collectionAddToContext.getProposed());
    }

}

// Copyright (c) Naked Objects Group Ltd.
