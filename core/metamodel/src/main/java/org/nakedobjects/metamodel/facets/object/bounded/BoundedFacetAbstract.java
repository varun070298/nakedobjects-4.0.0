package org.nakedobjects.metamodel.facets.object.bounded;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.interactions.UsabilityContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class BoundedFacetAbstract extends MarkerFacetAbstract implements BoundedFacet {

    public static Class<? extends Facet> type() {
        return BoundedFacet.class;
    }

    public BoundedFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

    /**
     * Hook method for subclasses to override.
     */
    public abstract String disabledReason(NakedObject no);

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof ObjectValidityContext)) {
            return null;
        }
        final NakedObject target = context.getTarget();
        return disabledReason(target);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> context) {
        final NakedObject target = context.getTarget();
        return disabledReason(target);
    }

}
