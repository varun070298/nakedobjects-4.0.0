package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.ActionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ParameterContent;


public class CollectionActionContent extends AbstractCollectionContent implements ActionContent {
    private final ActionHelper invocation;
    private final ParameterContent[] parameters;

    public CollectionActionContent(final ActionHelper invocation) {
        this.invocation = invocation;
        parameters = invocation.createParameters();
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.appendln("action", getActionName());
        debug.appendln("target", getNaked());
        String parameterSet = "";
        for (int i = 0; i < parameters.length; i++) {
            parameterSet += parameters[i];
        }
        debug.appendln("parameters", parameterSet);
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public Consent disabled() {
        return invocation.disabled();
    }

    public NakedObject drop(final Content sourceContent) {
        throw new NotYetImplementedException();
    }

    @Override
    public NakedObject[] elements() {
        throw new NotYetImplementedException();
    }

    public NakedObject execute() {
        return invocation.invoke();
    }

    public String getActionName() {
        return invocation.getName();
    }

    @Override
    public NakedObject getCollection() {
        return invocation.getTarget();
    }

    @Override
    public String getDescription() {
        return invocation.getDescription();
    }

    public String getHelp() {
        return invocation.getHelp();
    }

    public String getIconName() {
        return getNaked().getIconName();
    }

    public String getId() {
        return invocation.getName();
    }

    public NakedObject getNaked() {
        return invocation.getTarget();
    }

    public int getNoParameters() {
        return parameters.length;
    }

    public ParameterContent getParameterContent(final int index) {
        return parameters[index];
    }

    public NakedObject getParameterObject(final int index) {
        return invocation.getParameter(index);
    }

    public NakedObjectSpecification getSpecification() {
        return getNaked().getSpecification();
    }

    public boolean isTransient() {
        return true;
    }

    public String title() {
        return getNaked().titleString();
    }

    @Override
    public String windowTitle() {
        return getActionName();
    }
}
// Copyright (c) Naked Objects Group Ltd.
