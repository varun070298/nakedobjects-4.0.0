package org.nakedobjects.plugins.html.crumb;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.plugins.html.task.Task;


public class TaskCrumb implements Crumb {
    private final Task task;

    public TaskCrumb(final Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void debug(final DebugString string) {
        string.appendln("Task Crumb");
        string.appendln("task", task);

        task.debug(string);
    }

    public String title() {
        return task.getName();
    }

    @Override
    public String toString() {
        return new ToString(this).append(title()).toString();
    }

    public Request changeContext() {
        return ForwardRequest.task(task);
    }

}

// Copyright (c) Naked Objects Group Ltd.
