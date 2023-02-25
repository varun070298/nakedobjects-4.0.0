package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleWhenValueFacetAbstract;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public abstract class DisabledFacetAbstract extends SingleWhenValueFacetAbstract implements DisabledFacet {

    public static Class<? extends Facet> type() {
        return DisabledFacet.class;
    }

    public DisabledFacetAbstract(final When value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final NakedObject target = ic.getTarget();
        String disabledReason = disabledReason(target);
        if (disabledReason != null) {
        	return disabledReason;
        }
        if (getUnderlyingFacet() != null) {
        	DisabledFacet underlyingFacet = (DisabledFacet) getUnderlyingFacet();
        	return underlyingFacet.disabledReason(target);
        }
        return null;
    }

}
