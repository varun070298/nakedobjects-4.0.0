package org.nakedobjects.metamodel.facets.object.validprops;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;


public class ObjectValidPropertiesFacetImpl extends ObjectValidPropertiesFacetAbstract {

    public ObjectValidPropertiesFacetImpl(final FacetHolder holder) {
        super(holder);
    }

    public String invalidReason(final ObjectValidityContext context) {
        final StringBuilder buf = new StringBuilder();
        final NakedObject nakedObject = context.getTarget();
        for (final NakedObjectAssociation property : nakedObject.getSpecification().getAssociations(
                NakedObjectAssociationFilters.PROPERTIES)) {
            // ignore hidden properties
            if (property.isVisible(context.getSession(), nakedObject).isVetoed()) {
                continue;
            }
            // ignore disabled properties
            if (property.isUsable(context.getSession(), nakedObject).isVetoed()) {
                continue;
            }
            final OneToOneAssociation otoa = (OneToOneAssociation) property;
            final NakedObject value = otoa.get(nakedObject);
            if (otoa.isAssociationValid(nakedObject, value).isVetoed()) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(property.getName());
            }
        }
        if (buf.length() > 0) {
            return "Invalid properties: " + buf.toString();
        }
        return null;
    }

}

// Copyright (c) Naked Objects Group Ltd.
