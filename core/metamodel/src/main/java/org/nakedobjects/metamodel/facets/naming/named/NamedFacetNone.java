package org.nakedobjects.metamodel.facets.naming.named;

import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Has a name of <tt>null</tt>.
 * 
 * <p>
 * TODO: should this instead be the empty string?
 */
public class NamedFacetNone extends NamedFacetAbstract {

    public NamedFacetNone(final FacetHolder holder) {
        super(null, holder);
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
