package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class MaskFacetAbstract extends SingleStringValueFacetAbstract implements MaskFacet {

    public static Class<? extends Facet> type() {
        return MaskFacet.class;
    }

    public MaskFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof ProposedHolder)) {
            return null;
        }
        final ProposedHolder proposedHolder = (ProposedHolder) context;
        final NakedObject proposedArgument = proposedHolder.getProposed();
        if (doesNotMatch(proposedArgument)) {
            return "'" + proposedArgument.titleString() + "' does not match the mask '" + value() + "'";
        }
        return null;
    }

}
