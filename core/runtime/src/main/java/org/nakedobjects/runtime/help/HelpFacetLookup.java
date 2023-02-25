package org.nakedobjects.runtime.help;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.help.HelpFacetAbstract;


/**
 * Looked up via the {@link HelpManager}.
 */
public class HelpFacetLookup extends HelpFacetAbstract {

    public HelpFacetLookup(final String value, final FacetHolder holder) {
        super(value, holder);
    }

}

// Copyright (c) Naked Objects Group Ltd.
