package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;


public class RootObject extends AbstractObjectContent {
    private final NakedObject adapter;

    public RootObject(final NakedObject adapter) {
        this.adapter = adapter;
    }

    @Override
    public Consent canClear() {
        return Veto.DEFAULT;
    }

    @Override
    public Consent canDrop(final Content sourceContent) {
        return super.canDrop(sourceContent);
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
        debug.appendln("object", adapter);
    }

    public NakedObject getNaked() {
        return adapter;
    }

    public String getDescription() {
        return getSpecification().getSingularName() + ": " + getObject().titleString() + " "
                + getSpecification().getDescription();
    }

    public String getHelp() {
        return "";
    }

    public String getId() {
        return "";
    }

    @Override
    public NakedObject getObject() {
        return adapter;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return adapter.getSpecification();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isTransient() {
        return adapter != null && adapter.isTransient();
    }

    @Override
    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public String title() {
        return adapter.titleString();
    }

    @Override
    public String toString() {
        return "Root Object [adapter=" + adapter + "]";
    }

    @Override
    public String windowTitle() {
        return (isTransient() ? "UNSAVED " : "") + getSpecification().getSingularName();
    }
}
// Copyright (c) Naked Objects Group Ltd.
