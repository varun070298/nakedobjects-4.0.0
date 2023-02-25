package org.nakedobjects.metamodel.facets.propcoll.derived;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public abstract class DerivedFacetAbstract extends MarkerFacetAbstract implements DerivedFacet {

    public static Class<? extends Facet> type() {
        return DerivedFacet.class;
    }

    public DerivedFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

    /**
     * Always returns <i>Derived</i>.
     */
    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        return "Derived";
    }
}
