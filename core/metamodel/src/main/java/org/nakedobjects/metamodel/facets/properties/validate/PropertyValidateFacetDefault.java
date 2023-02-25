package org.nakedobjects.metamodel.facets.properties.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ValidityContext;


/**
 * Non checking property validation facet that provides default behaviour for all properties.
 */
public class PropertyValidateFacetDefault extends FacetAbstract implements PropertyValidateFacet {

    public String invalidates(final ValidityContext<? extends ValidityEvent> ic) {
        return null;
    }

    public PropertyValidateFacetDefault(final FacetHolder holder) {
        super(PropertyValidateFacet.class, holder, false);
    }

    public String invalidReason(final NakedObject target, final NakedObject proposedValue) {
        return null;
    }

}

// Copyright (c) Naked Objects Group Ltd.
