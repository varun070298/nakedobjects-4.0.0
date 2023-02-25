package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleWhenValueFacetAbstract;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public abstract class ImmutableFacetAbstract extends SingleWhenValueFacetAbstract implements ImmutableFacet {

    public static Class<? extends Facet> type() {
        return ImmutableFacet.class;
    }

    public ImmutableFacetAbstract(final When value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    /**
     * Hook method for subclasses to override.
     */
    public abstract String disabledReason(NakedObject no);

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final NakedObject target = ic.getTarget();
        return disabledReason(target);
    }

}
