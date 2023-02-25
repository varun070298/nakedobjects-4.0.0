package org.nakedobjects.plugins.html.action.edit;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.plugins.html.task.AddItemToCollectionTask;



public class AddItemToCollection implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final String collectionField = request.getProperty();

        final String idString = request.getObjectId();
        if (idString == null) {
            throw new ActionException("Task no longer in progress");
        }
        final NakedObject object = context.getMappedObject(idString);
        final NakedObjectSpecification specification = object.getSpecification();
        final OneToManyAssociation field = (OneToManyAssociation) specification.getAssociation(collectionField);
        final AddItemToCollectionTask addTask = new AddItemToCollectionTask(context, object, field);
        context.addTaskCrumb(addTask);
        request.forward(ForwardRequest.task(addTask));
    }

    public String name() {
        return "add";
    }

}

// Copyright (c) Naked Objects Group Ltd.
