package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleWhenValueFacetAbstract;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.interactions.VisibilityContext;


public abstract class HiddenFacetAbstract extends SingleWhenValueFacetAbstract implements HiddenFacet {

    public static Class<? extends Facet> type() {
        return HiddenFacet.class;
    }

    public HiddenFacetAbstract(final When value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    public String hides(final VisibilityContext<? extends VisibilityEvent> ic) {
        return hiddenReason(ic.getTarget());
    }

}
