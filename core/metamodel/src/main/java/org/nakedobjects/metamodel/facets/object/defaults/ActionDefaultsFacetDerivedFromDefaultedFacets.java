package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacetAbstract;


public class ActionDefaultsFacetDerivedFromDefaultedFacets extends ActionDefaultsFacetAbstract {

    private final DefaultedFacet[] defaultedFacets;

    public ActionDefaultsFacetDerivedFromDefaultedFacets(final DefaultedFacet[] defaultedFacets, final FacetHolder holder) {
        super(holder, true);
        this.defaultedFacets = defaultedFacets;
    }

    /**
     * Return the defaults.
     * 
     * <p>
     * Note that we get the defaults fresh each time in case the defaults might conceivably change.
     */
    public Object[] getDefaults(final NakedObject inObject) {
        final Object[] defaults = new Object[defaultedFacets.length];
        for (int i = 0; i < defaults.length; i++) {
            if (defaultedFacets[i] != null) {
                defaults[i] = defaultedFacets[i].getDefault();
            }
        }
        return defaults;
    }

}

// Copyright (c) Naked Objects Group Ltd.
