package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.UsabilityContext;


public class NotPersistableFacetImpl extends NotPersistableFacetAbstract {

    public NotPersistableFacetImpl(final InitiatedBy value, final FacetHolder holder) {
        super(value, holder);
    }

    public String disables(final UsabilityContext<? extends UsabilityEvent> ic) {
        final InitiatedBy by = value();
        if (ic.isProgrammatic() && ic.equals(InitiatedBy.USER)) {
            return null;
        }
        return "Not persistable";
    }

}

// Copyright (c) Naked Objects Group Ltd.
