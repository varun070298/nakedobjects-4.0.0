package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.ActionContent;
import org.nakedobjects.plugins.dnd.ParameterContent;


/**
 * Links an action on an object to a view.
 */
public class ObjectActionContent extends AbstractObjectContent implements ActionContent {
    private final ActionHelper actionHelper;
    private final ParameterContent[] parameters;

    public ObjectActionContent(final ActionHelper invocation) {
        this.actionHelper = invocation;
        parameters = invocation.createParameters();
    }

    @Override
    public Consent canClear() {
        return Veto.DEFAULT;
    }

    @Override
    public Consent canSet(final NakedObject dragSource) {
        return Veto.DEFAULT;
    }

    @Override
    public void clear() {
        throw new NakedObjectException("Invalid call");
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("action", getActionName());
        debug.appendln("target", getNaked());
        String parameterSet = "";
        for (int i = 0; i < parameters.length; i++) {
            parameterSet += parameters[i];
        }
        debug.appendln("parameters", parameterSet);
    }

    public Consent disabled() {
        return actionHelper.disabled();
    }

    public NakedObject execute() {
        return actionHelper.invoke();
    }

    public String getActionName() {
        return actionHelper.getName();
    }

    @Override
    public String getIconName() {
        return actionHelper.getIconName();
    }

    public NakedObject getNaked() {
        return actionHelper.getTarget();
    }

    public int getNoParameters() {
        return parameters.length;
    }

    @Override
    public NakedObject getObject() {
        return actionHelper.getTarget();
    }

    public ParameterContent getParameterContent(final int index) {
        return parameters[index];
    }

    public NakedObject getParameterObject(final int index) {
        return actionHelper.getParameter(index);
    }

    public NakedObjectSpecification getSpecification() {
        return getObject().getSpecification();
    }

    /**
     * Can't persist actions
     */
    @Override
    public boolean isPersistable() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isTransient() {
        return true;
    }

    @Override
    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public String title() {
        return actionHelper.title();
    }

    @Override
    public String windowTitle() {
        return getActionName();
    }

    public String getId() {
        return actionHelper.getName();
    }

    public String getDescription() {
        return actionHelper.getDescription();
    }

    public String getHelp() {
        return actionHelper.getHelp();
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public boolean isOptionEnabled() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
