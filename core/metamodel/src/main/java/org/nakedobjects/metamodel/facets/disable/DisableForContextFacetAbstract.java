package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public abstract class DisableForContextFacetAbstract extends FacetAbstract implements DisableForContextFacet {

    public static Class<? extends Facet> type() {
        return DisableForContextFacet.class;
    }

    public DisableForContextFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final NakedObject target = ic.getTarget();
        return disabledReason(target);
    }

}

// Copyright (c) Naked Objects Group Ltd.
