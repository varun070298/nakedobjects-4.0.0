package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.VisibilityContext;


/**
 * Hide a property, collection or action based on the current session.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>hideXxx(UserMemento)</tt>
 * support method for the member.
 */
public abstract class HideForSessionFacetAbstract extends FacetAbstract implements HideForSessionFacet {

    public static Class<? extends Facet> type() {
        return HideForSessionFacet.class;
    }

    public HideForSessionFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String hides(final VisibilityContext<? extends VisibilityEvent> ic) {
        return hiddenReason(ic.getSession());
    }
}

// Copyright (c) Naked Objects Group Ltd.
