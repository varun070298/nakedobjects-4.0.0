package org.nakedobjects.metamodel.facets.object.ident.title;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;


public class TitleFacetNone extends TitleFacetAbstract {

    public TitleFacetNone(final FacetHolder holder) {
        super(holder);
    }

    public String title(final NakedObject object) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
