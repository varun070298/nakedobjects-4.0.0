package org.nakedobjects.metamodel.facets.properties.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.PropertyModifyContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;


public abstract class PropertyValidateFacetAbstract extends FacetAbstract implements PropertyValidateFacet {

    public static Class<? extends Facet> type() {
        return PropertyValidateFacet.class;
    }

    public PropertyValidateFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof PropertyModifyContext)) {
            return null;
        }
        final PropertyModifyContext propertyModifyContext = (PropertyModifyContext) context;
        return invalidReason(propertyModifyContext.getTarget(), propertyModifyContext.getProposed());
    }
}

// Copyright (c) Naked Objects Group Ltd.
