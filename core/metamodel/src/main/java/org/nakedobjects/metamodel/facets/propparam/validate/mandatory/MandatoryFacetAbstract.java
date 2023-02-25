package org.nakedobjects.metamodel.facets.propparam.validate.mandatory;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;
import org.nakedobjects.metamodel.interactions.ActionArgumentContext;
import org.nakedobjects.metamodel.interactions.PropertyModifyContext;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class MandatoryFacetAbstract extends MarkerFacetAbstract implements MandatoryFacet {

    public static Class<? extends Facet> type() {
        return MandatoryFacet.class;
    }

    public MandatoryFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof PropertyModifyContext) && !(context instanceof ActionArgumentContext)) {
            return null;
        }
        if (!(context instanceof ProposedHolder)) {
            // shouldn't happen, since both the above should hold a proposed value/argument
            return null;
        }
        final ProposedHolder proposedHolder = (ProposedHolder) context;
        return isRequiredButNull(proposedHolder.getProposed()) ? "Mandatory" : null;
    }
}
