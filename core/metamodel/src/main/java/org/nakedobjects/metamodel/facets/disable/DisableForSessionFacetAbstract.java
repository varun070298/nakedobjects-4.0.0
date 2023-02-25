package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public abstract class DisableForSessionFacetAbstract extends FacetAbstract implements DisableForSessionFacet {

    public static Class<? extends Facet> type() {
        return DisableForSessionFacet.class;
    }

    public DisableForSessionFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final AuthenticationSession session = ic.getSession();
        return disabledReason(session);
    }

}

// Copyright (c) Naked Objects Group Ltd.
