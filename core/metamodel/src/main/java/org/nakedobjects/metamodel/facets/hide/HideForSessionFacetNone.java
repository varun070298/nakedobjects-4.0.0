package org.nakedobjects.metamodel.facets.hide;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class HideForSessionFacetNone extends HideForSessionFacetAbstract {

    public HideForSessionFacetNone(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public String hiddenReason(final AuthenticationSession session) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
