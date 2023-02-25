package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToOneActionParameter;
import org.nakedobjects.plugins.dnd.ObjectParameter;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;


public class ObjectParameterImpl extends AbstractObjectContent implements ObjectParameter {
    private final NakedObject adapter;
    private final ActionHelper invocation;
    private final int index;
    private final NakedObject[] optionAdapters;
    private final OneToOneActionParameter nakedObjectActionParameter;

    public ObjectParameterImpl(
            final OneToOneActionParameter nakedObjectActionParameter,
            final NakedObject adapter,
            final NakedObject[] optionAdapters,
            final int i,
            final ActionHelper invocation) {
        this.nakedObjectActionParameter = nakedObjectActionParameter;
        this.optionAdapters = optionAdapters;
        this.index = i;
        this.invocation = invocation;
        this.adapter = adapter;
    }

    public ObjectParameterImpl(final ObjectParameterImpl content, final NakedObject object) {
        nakedObjectActionParameter = content.nakedObjectActionParameter;
        optionAdapters = content.optionAdapters;
        index = content.index;
        invocation = content.invocation;
        this.adapter = object;
    }

    @Override
    public Consent canClear() {
        return Allow.DEFAULT;
    }

    @Override
    public Consent canSet(final NakedObject dragSource) {
        if (dragSource.getSpecification().isOfType(getSpecification())) {
            // TODO: move logic into Facet
            return Allow.DEFAULT;
        } else {
            // TODO: move logic into Facet
            return new Veto(String.format("Object must be ", getSpecification().getShortName()));
        }
    }

    @Override
    public void clear() {
        setObject(null);
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("name", getParameterName());
        debug.appendln("required", isRequired());
        debug.appendln("object", adapter);
    }

    public NakedObject getNaked() {
        return adapter;
    }

    @Override
    public NakedObject getObject() {
        return adapter;
    }

    public NakedObject[] getOptions() {
        return optionAdapters;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isRequired() {
        return !nakedObjectActionParameter.isOptional();
    }

    @Override
    public boolean isPersistable() {
        return false;
    }

    public boolean isOptionEnabled() {
        return optionAdapters != null && optionAdapters.length > 0;
    }

    public boolean isTransient() {
        return adapter != null && adapter.isTransient();
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        if (adapter != null) {
            options.add(new AbstractUserAction("Clear parameter") {

                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    clear();
                    view.getParent().invalidateContent();
                }
            });

            OptionFactory.addObjectMenuOptions(adapter, options);
        } else {
            OptionFactory.addCreateOptions(getSpecification(), options);

        }

    }

    @Override
    public void setObject(final NakedObject object) {
        invocation.setParameter(index, object);
    }

    public String title() {
        return adapter == null ? "" : adapter.titleString();
    }

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        toString.append("label", getParameterName());
        toString.append("required", isRequired());
        toString.append("spec", getSpecification().getFullName());
        toString.append("object", adapter == null ? "null" : adapter.titleString());
        return toString.toString();
    }

    public String getParameterName() {
        return nakedObjectActionParameter.getName();
    }

    public NakedObjectSpecification getSpecification() {
        return nakedObjectActionParameter.getSpecification();
    }

    public String getDescription() {
        final String title = adapter == null ? "" : ": " + adapter.titleString();
        final String name = getParameterName();
        final NakedObjectSpecification specification = nakedObjectActionParameter.getSpecification();
        final String specName = specification.getShortName();
        final String type = name.indexOf(specName) == -1 ? " (" + specName + ")" : "";
        return name + type + title + " " + nakedObjectActionParameter.getDescription();
    }

    public String getHelp() {
        return invocation.getHelp();
    }

    public String getId() {
        return null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
