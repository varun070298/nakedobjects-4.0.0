package org.nakedobjects.metamodel.facets.actions.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ActionInvocationContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class ActionValidationFacetAbstract extends FacetAbstract implements ActionValidationFacet {

    public static Class<? extends Facet> type() {
        return ActionValidationFacet.class;
    }

    public ActionValidationFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof ActionInvocationContext)) {
            return null;
        }
        final ActionInvocationContext actionInvocationContext = (ActionInvocationContext) context;
        return invalidReason(actionInvocationContext.getTarget(), actionInvocationContext.getArgs());
    }
}
