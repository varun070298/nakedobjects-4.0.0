package org.nakedobjects.plugins.html.request;

import org.nakedobjects.plugins.html.task.Task;


public class ForwardRequest implements Request {

    public static Request editObject(final String objectId) {
        return new ForwardRequest(EDIT_COMMAND, objectId);
    }

    public static Request listCollection(final String collectionId) {
        return new ForwardRequest(COLLECTION_COMMAND, collectionId);
    }

    public static ForwardRequest viewObject(final String objectId) {
        return new ForwardRequest(OBJECT_COMMAND, objectId);
    }

    public static Request viewObject(final String objectId, final String collectionField) {
        return new ForwardRequest(OBJECT_COMMAND, objectId, collectionField);
    }

    public static Request viewService(final String objectId) {
        return new ForwardRequest(SERVICE_COMMAND, objectId);
    }

    public static Request task(final Task task) {
        final ForwardRequest forwardRequest = new ForwardRequest(TASK_COMMAND, null);
        forwardRequest.taskId = task.getId();
        return forwardRequest;
    }

    public static Request taskComplete() {
        final ForwardRequest forwardRequest = new ForwardRequest(TASK_COMMAND, null);
        forwardRequest.submitName = "Ok";
        return forwardRequest;
    }

    private final String actionName;
    private Request forwardedRequest;
    private final String objectId;
    private final String fieldName;
    private String submitName;
    private String taskId;

    private ForwardRequest(final String actionName, final String id) {
        this(actionName, id, null);
    }

    private ForwardRequest(final String actionName, final String objectId, final String fieldName) {
        this.actionName = actionName;
        this.objectId = objectId;
        this.fieldName = fieldName;
    }

    public void forward(final Request forwardedRequest) {
        this.forwardedRequest = forwardedRequest;
    }

    public String getActionId() {
        return null;
    }

    public String getElementId() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getRequestType() {
        return actionName;
    }

    public String getButtonName() {
        return submitName;
    }

    public String getProperty() {
        return fieldName;
    }

    public String getFieldEntry(final int i) {
        return null;
    }

    public String getTaskId() {
        return taskId;
    }

    public Request getForward() {
        return forwardedRequest;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "ForwardRequest " + actionName + " " + forwardedRequest;
    }

    public static Request logon() {
        return new ForwardRequest("logon", null);
    }
}

// Copyright (c) Naked Objects Group Ltd.
