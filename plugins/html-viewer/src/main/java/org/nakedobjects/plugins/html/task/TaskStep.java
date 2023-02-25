package org.nakedobjects.plugins.html.task;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.Form;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;


public final class TaskStep implements Action {
    private void addSelector(
            final Context context,
            final Form form,
            final String currentEntry,
            final String fieldId,
            final String fieldLabel,
            final String fieldDescription,
            final boolean required,
            final String errorMessage,
            final Task task,
            final NakedObject[] objects) {
        task.checkInstances(context, objects);

        int size = 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                size++;
            }
        }

        final String[] instances = new String[size];
        final String[] ids = new String[size];
        int selectedIndex = -1;

        for (int i = 0, j = 0; i < objects.length; i++) {
            final NakedObject element = objects[i];
            if (element != null) {
                instances[j] = element.titleString();
                ids[j] = context.mapObject(element);
                if (ids[j].equals(currentEntry)) {
                    selectedIndex = i;
                }
                j++;
            }
        }
        form.addLookup(fieldLabel, fieldDescription, fieldId, selectedIndex, instances, ids, required, errorMessage);
    }

    private void addSelectorForKnownReferences(
            final Context context,
            final Form form,
            final NakedObjectSpecification type,
            final String currentEntry,
            final String fieldId,
            final String fieldLabel,
            final String fieldDescription,
            final boolean required,
            final String errorMessage,
            final Task task) {

        final NakedObject[] objects = context.getKnownInstances(type);
        addSelector(context, form, currentEntry, fieldId, fieldLabel, fieldDescription, required, errorMessage, task, objects);
    }

    private void addSelectorForObjectOptions(
            final Context context,
            final Form form,
            final String currentEntry,
            final String fieldId,
            final String fieldLabel,
            final String fieldDescription,
            final NakedObject[] options,
            final boolean required,
            final String errorMessage,
            final Task task) {
        final NakedObject[] objects = new NakedObject[options.length];
        for (int i = 0; i < options.length; i++) {
            objects[i] = options[i];
        }
        addSelector(context, form, currentEntry, fieldId, fieldLabel, fieldDescription, required, errorMessage, task, objects);
    }

    private void addSelectorForValueOptions(
            final Form form,
            final String currentEntry,
            final String fieldId,
            final String fieldLabel,
            final String fieldDescription,
            final NakedObject[] options,
            final boolean required,
            final String errorMessage,
            final Task task) {
        int selectedIndex = -1;
        final String[] instances = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            instances[i] = options[i].titleString();
            if (currentEntry.equals(instances[i])) {
                selectedIndex = i;
            }
        }
        form.addLookup(fieldLabel, fieldDescription, fieldId, selectedIndex, instances, instances, required, errorMessage);
    }

    private void addTextFieldForParseable(
            final Form form,
            final NakedObjectSpecification type,
            final String currentEntryText,
            final String fieldId,
            final String fieldLabel,
            final String fieldDescription,
            final int noLines,
            final boolean wrap,
            final int maxLength,
            final int typicalLength,
            final boolean required,
            final String errorMessage) {
        form.addField(type, fieldLabel, fieldDescription, fieldId, currentEntryText, noLines, wrap, maxLength, typicalLength,
                required, errorMessage);
    }

    private void displayTask(final Context context, final Page page, final Task task) {
        page.setTitle(task.getName());

        final ViewPane content = page.getViewPane();
        final NakedObject targetAdapter = task.getTarget(context);
        String titleString = targetAdapter.titleString();
        if (targetAdapter.isTransient()) {
            titleString += " (Unsaved)";
        }
        content.setTitle(titleString, targetAdapter.getSpecification().getDescription());
        String iconName = targetAdapter.getIconName();
        if (iconName == null) {
            iconName = targetAdapter.getSpecification().getShortName();
        }
        content.setIconName(iconName);

        final StringBuffer crumbs = new StringBuffer();
        final String[] trail = task.getTrail();
        for (int i = 0; i < trail.length; i++) {
            crumbs.append(" : ");
            crumbs.append(trail[i]);
        }

        final Component[] action = new Component[1];
        action[0] = context.getComponentFactory().createInlineBlock("name", task.getName(), task.getDescription());
        content.setMenu(action);

        if (task.getError() != null) {
            content.add(context.getComponentFactory().createInlineBlock("error", task.getError(), null));
        }

        final Form form = context.getComponentFactory().createForm(task.getId(), name(), task.getStep(), task.numberOfSteps(),
                task.isEditing());
        final String[] parameterLabels = task.getNames();
        final String[] parameterDescriptions = task.getFieldDescriptions();
        final String[] errors = task.getErrors();
        final String[] entryText = task.getEntryText();
        final int[] noLines = task.getNoLines();
        final boolean[] canWrap = task.getWraps();
        final int[] maxLength = task.getMaxLength();
        final int[] typicalLength = task.getTypicalLength();
        final NakedObject[][] options = task.getOptions(context);
        final boolean[] optional = task.getOptional();
        final boolean[] readOnly = task.getReadOnly();
        final NakedObjectSpecification[] types = task.getTypes();
        for (int i = 0; i < parameterLabels.length; i++) {
            final NakedObjectSpecification paramSpec = types[i];
            final String fieldId = "fld" + i;
            final String fieldLabel = parameterLabels[i] == null ? "" : parameterLabels[i];
            ;
            final String fieldDescription = parameterDescriptions[i] == null ? "" : parameterDescriptions[i];
            final String currentEntryTitle = entryText[i];
            final String error = errors[i];
            if (readOnly[i]) {
                addReadOnlyField(form, fieldLabel, currentEntryTitle, fieldDescription);
            } else if (paramSpec.isParseable() && options[i] != null && options[i].length > 0) {
                addSelectorForValueOptions(form, currentEntryTitle, fieldId, fieldLabel, fieldDescription, options[i],
                        !optional[i], error, task);
            } else if (paramSpec.isParseable()) {
                addTextFieldForParseable(form, paramSpec, currentEntryTitle, fieldId, fieldLabel, fieldDescription, noLines[i],
                        canWrap[i], maxLength[i], typicalLength[i], !optional[i], error);
            } else if (paramSpec.isObject() && options[i] != null && options[i].length > 0) {
                addSelectorForObjectOptions(context, form, currentEntryTitle, fieldId, fieldLabel, fieldDescription, options[i],
                        !optional[i], error, task);
            } else if (paramSpec.isObject()) {
                addSelectorForKnownReferences(context, form, paramSpec, currentEntryTitle, fieldId, fieldLabel, fieldDescription,
                        !optional[i], error, task);
            } else {
                throw new NakedObjectException();
            }
        }
        content.add(form);
    }

    private void addReadOnlyField(final Form form, final String fieldLabel, final String title, final String description) {
        form.addReadOnlyField(fieldLabel, title, description);
    }

    public void execute(final Request request, final Context context, final Page page) {
        final String taskId = request.getTaskId();
        final Task task = context.getTask(taskId);
        final String button = request.getButtonName();
        if (task == null && !"Cancel".equals(button)) {
            throw new TaskLookupException("No task found with id " + taskId);
        }

        if (button == null) {
            // start new task
            displayTask(context, page, task);
        } else if ("Cancel".equals(button)) {
            forwardCancel(request, context, task);
        } else if ("Previous".equals(button)) {
            task.setFromFields(request, context);
            task.previousStep();
            displayTask(context, page, task);
        } else if ("Next".equals(button)) {
            task.setFromFields(request, context);
            task.nextStep();
            displayTask(context, page, task);
        } else if ("Finish".equals(button) || "Save".equals(button) || "Ok".equals(button)) {
            task.setFromFields(request, context);
            task.checkForValidity(context);

            if (hasErrors(task)) {
                displayTask(context, page, task);
            } else {
                final String targetId = context.mapObject(task.getTarget(context));
                final NakedObject result = task.completeTask(context, page);
                if (result instanceof NakedObject) {
                    final NakedObject object = result;
                    context.updateVersion(object);
                }
                InvokeMethod.displayMethodResult(request, context, page, result, targetId);
                context.endTask(task);
            }
        } else {
            throw new ActionException("No task action: " + button);
        }
    }

    private void forwardCancel(final Request request, final Context context, final Task task) {
        final Request cancelTask = context.cancelTask(task);
        request.forward(cancelTask);
    }

    private boolean hasErrors(final Task task) {
        if (task.getError() != null) {
            return true;
        }
        final String[] errors = task.getErrors();
        for (int i = 0; i < errors.length; i++) {
            if (errors[i] != null) {
                return true;
            }
        }
        return false;
    }

    public String name() {
        return Request.TASK_COMMAND;
    }
}

// Copyright (c) Naked Objects Group Ltd.
