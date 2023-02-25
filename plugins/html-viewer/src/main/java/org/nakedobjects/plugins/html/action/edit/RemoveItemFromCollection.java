package org.nakedobjects.plugins.html.action.edit;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;



public class RemoveItemFromCollection implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final String objectId = request.getObjectId();
        final String elementId = request.getElementId();
        final String collectionField = request.getProperty();

        final NakedObject target = context.getMappedObject(objectId);
        final NakedObject element = context.getMappedObject(elementId);
        final NakedObjectSpecification specification = target.getSpecification();
        final OneToManyAssociation field = (OneToManyAssociation) specification.getAssociation(collectionField);
        field.removeElement(target, element);

        request.forward(ForwardRequest.viewObject(objectId, collectionField));
    }

    public String name() {
        return "remove";
    }

}

// Copyright (c) Naked Objects Group Ltd.
