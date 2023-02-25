package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class DisableForSessionFacetNone extends DisableForSessionFacetAbstract {

    public DisableForSessionFacetNone(final FacetHolder holder) {
        super(holder);
    }

    public String disabledReason(final AuthenticationSession session) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
