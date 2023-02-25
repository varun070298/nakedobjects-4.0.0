package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;


public interface ActionContent extends Content {
    public Consent disabled();

    public NakedObject execute();

    public String getActionName();

    public int getNoParameters();

    public ParameterContent getParameterContent(final int index);

    public NakedObject getParameterObject(final int index);

    public String getDescription();
}
// Copyright (c) Naked Objects Group Ltd.
