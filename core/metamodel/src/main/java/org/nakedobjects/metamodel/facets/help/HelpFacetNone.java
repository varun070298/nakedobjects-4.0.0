package org.nakedobjects.metamodel.facets.help;

import org.nakedobjects.metamodel.facets.FacetHolder;


/**
 * Has a description of <tt>null</tt>.
 */
public class HelpFacetNone extends HelpFacetAbstract {

    public HelpFacetNone(final FacetHolder holder) {
        super(null, holder);
    }

    @Override
    public String value() {
        return "No help available";
    }

    @Override
    public boolean isNoop() {
        return true;
    }

}

// Copyright (c) Naked Objects Group Ltd.
