package org.nakedobjects.plugins.html.task;

import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;


public final class InvokeMethod implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final String idString = request.getObjectId();
        if (idString == null) {
            throw new ActionException("Task no longer in progress");
        }
        final NakedObject target = context.getMappedObject(idString);
        final String id = request.getActionId();
        final NakedObjectAction action = context.getMappedAction(id);
        if (action == null) {
            throw new ActionException("No such action: " + id);
        }

        boolean executeImmediately = false;
        // TODO use new promptForParameters method instead of all this
        final boolean isContributedMethod = action.isContributed();
        if (action.getParameterCount() == 0) {
            executeImmediately = true;
        } else if (action.getParameterCount() == 1 && isContributedMethod
                && target.getSpecification().isOfType(action.getParameters()[0].getSpecification())) {
            executeImmediately = true;
        }

        if (executeImmediately) {
            final NakedObject[] parameters = isContributedMethod ? new NakedObject[] { target } : null;
            final NakedObject result = action.execute(target, parameters);
            final MessageBroker broker = NakedObjectsContext.getMessageBroker();
            final List<String> messages = broker.getMessages();
            final List<String> warnings = broker.getWarnings();
            context.setMessagesAndWarnings(messages, warnings);
            context.processChanges();
            final String targetId = context.mapObject(target);
            displayMethodResult(request, context, page, result, targetId);
        } else {
            final MethodTask methodTask = new MethodTask(context, target, action);
            context.addTaskCrumb(methodTask);
            request.forward(ForwardRequest.task(methodTask));
        }
    }

    static void displayMethodResult(
            final Request request,
            final Context context,
            final Page page,
            final NakedObject result,
            final String targetId) {
        if (result == null) {
            // TODO ask context for page to display - this will be the most recent object prior to the task
            // null object - so just view service
            request.forward(ForwardRequest.viewService(targetId));
        } else {
            if (result.getSpecification().isCollection()) {
                final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(result);
                if (facet.size(result) == 1) {
                    forwardObjectResult(request, context, facet.firstElement(result));
                } else {
                    forwardCollectionResult(request, context, result);
                }
            } else if (result.getSpecification().isValueOrIsAggregated()) {
                // TODO deal with this object properly, it might not be just a simple string
                List<String> messages = context.getMessages();
                messages.add(0, "Action returned: " + result.titleString());
                request.forward(ForwardRequest.viewObject(targetId));
            } else if (result.getSpecification().isObject()) {
                    forwardObjectResult(request, context, result);
            } else {
                throw new UnknownTypeException(result.getSpecification().getFullName());
            }
        }
    }

    static void forwardCollectionResult(final Request request, final Context context, final NakedObject coll) {
        final String collectionId = context.mapCollection(coll);
        request.forward(ForwardRequest.listCollection(collectionId));
    }

    static void forwardObjectResult(final Request request, final Context context, final NakedObject resultAdapter) {       
        final String objectId = context.mapObject(resultAdapter);
        if (resultAdapter.isTransient() && resultAdapter.getSpecification().persistability() == Persistability.USER_PERSISTABLE) {
            request.forward(ForwardRequest.editObject(objectId));
        } else if (resultAdapter.getSpecification().isService()) {
            request.forward(ForwardRequest.viewService(objectId));
        } else {
            request.forward(ForwardRequest.viewObject(objectId));
        }
    }

    public String name() {
        return "method";
    }

}

// Copyright (c) Naked Objects Group Ltd.
