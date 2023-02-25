package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.VisibilityContext;


public abstract class HideForContextFacetAbstract extends FacetAbstract implements HideForContextFacet {

    public static Class<? extends Facet> type() {
        return HideForContextFacet.class;
    }

    public HideForContextFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String hides(final VisibilityContext<? extends VisibilityEvent> ic) {
        return hiddenReason(ic.getTarget());
    }
}

// Copyright (c) Naked Objects Group Ltd.
