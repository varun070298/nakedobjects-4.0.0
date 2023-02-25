package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class CollectionElement extends AbstractObjectContent {
    private final NakedObject adapter;

    public CollectionElement(final NakedObject adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isOptionEnabled() {
        return false;
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
        debug.appendln("element", adapter);
    }

    public String getId() {
        return "";
    }

    public String getDescription() {
        return getSpecification().getSingularName() + ": " + getObject().titleString() + " "
                + getSpecification().getDescription();
    }

    public String getHelp() {
        return "";
    }

    @Override
    public NakedObject getObject() {
        return adapter;
    }

    public NakedObject getNaked() {
        return adapter;
    }

    public NakedObjectSpecification getSpecification() {
        return adapter.getSpecification();
    }

    public boolean isTransient() {
        return adapter.isTransient();
    }

    @Override
    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public String title() {
        return adapter.titleString();
    }

    @Override
    public String windowTitle() {
        return getSpecification().getShortName();
    }

    @Override
    public String toString() {
        return "" + adapter;
    }

    public NakedObject[] getOptions() {
        return null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
