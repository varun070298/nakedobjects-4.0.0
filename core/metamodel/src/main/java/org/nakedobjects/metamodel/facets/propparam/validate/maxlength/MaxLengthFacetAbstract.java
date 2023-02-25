package org.nakedobjects.metamodel.facets.propparam.validate.maxlength;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleIntValueFacetAbstract;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class MaxLengthFacetAbstract extends SingleIntValueFacetAbstract implements MaxLengthFacet {

    public static Class<? extends Facet> type() {
        return MaxLengthFacet.class;
    }

    public MaxLengthFacetAbstract(final int value, final FacetHolder holder) {
        super(type(), holder, value);
    }

    /**
     * Whether the provided argument exceeds the {@link #value() maximum length}.
     */
    public boolean exceeds(final NakedObject nakedObject) {
        final String str = unwrapString(nakedObject);
        if (str == null) {
            return false;
        }
        final int maxLength = value();
        return maxLength != 0 && str.length() > maxLength;
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof ProposedHolder)) {
            return null;
        }
        final ProposedHolder proposedHolder = (ProposedHolder) context;
        final NakedObject proposedArgument = proposedHolder.getProposed();
        if (!exceeds(proposedArgument)) {
            return null;
        }
        return "Proposed value '" + proposedArgument.titleString() + "' exceeds the maximum length of " + value();
    }

    @Override
    protected String toStringValues() {
        final int val = value();
        return val == 0 ? "unlimited" : String.valueOf(val);
    }
}
