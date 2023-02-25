package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class OneToOneFieldImpl extends AbstractObjectContent implements OneToOneField {
    private static final UserAction CLEAR_ASSOCIATION = new ClearOneToOneAssociationOption();
    private final ObjectField field;
    private final NakedObject adapter;

    public OneToOneFieldImpl(
    		final NakedObject parentAdapter, 
    		final NakedObject adapter, 
    		final OneToOneAssociation association) {
        field = new ObjectField(parentAdapter, association);
        this.adapter = adapter;
    }

    @Override
    public Consent canClear() {
        final NakedObject parentAdapter = getParent();
        final OneToOneAssociation association = getOneToOneAssociation();
        // NakedObject associatedObject = getObject();

        final Consent isEditable = isEditable();
        if (isEditable.isVetoed()) {
            return isEditable;
        }

        final Consent consent = association.isAssociationValid(parentAdapter, null);
        if (consent.isAllowed()) {
            consent.setDescription("Clear the association to this object from '" + parentAdapter.titleString() + "'");
        }
        return consent;
    }

    @Override
    public Consent canSet(final NakedObject adapter) {
        final NakedObjectSpecification targetType = getOneToOneAssociation().getSpecification();
        final NakedObjectSpecification spec = adapter.getSpecification();

        if (isEditable().isVetoed()) {
            return isEditable();
        }

        if (!spec.isOfType(targetType)) {
            // TODO: move logic into Facet
            return new Veto(String.format("Can only drop objects of type %s", targetType.getSingularName()));
        }

        if (getParent().isPersistent() && adapter.isTransient()) {
            // TODO: move logic into Facet
            return new Veto("Can't drop a non-persistent into this persistent object");
        }

        final Consent perm = getOneToOneAssociation().isAssociationValid(getParent(), adapter);
        return perm;
    }

    @Override
    public void clear() {
        getOneToOneAssociation().clearAssociation(getParent());
    }

    public void debugDetails(final DebugString debug) {
        field.debugDetails(debug);
        debug.appendln("object", adapter);
    }

    public String getFieldName() {
        return field.getName();
    }

    public NakedObjectAssociation getField() {
        return field.getNakedObjectAssociation();
    }

    public Consent isEditable() {
        return getField().isUsable(NakedObjectsContext.getAuthenticationSession(), getParent());
    }

    public NakedObject getNaked() {
        return adapter;
    }

    @Override
    public NakedObject getObject() {
        return adapter;
    }

    private OneToOneAssociation getOneToOneAssociation() {
        return (OneToOneAssociation) getField();
    }

    public NakedObject[] getOptions() {
        return getOneToOneAssociation().getChoices(getParent());
    }

    public NakedObject getParent() {
        return field.getParent();
    }

    public NakedObjectSpecification getSpecification() {
        return getOneToOneAssociation().getSpecification();
    }

    public boolean isMandatory() {
        return getOneToOneAssociation().isMandatory();
    }
    
    @Override
    public boolean isPersistable() {
        return getObject() != null && super.isPersistable();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isOptionEnabled() {
        return getOneToOneAssociation().hasChoices();
    }

    public boolean isTransient() {
        return adapter != null && adapter.isTransient();
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);
        if (getObject() != null && !getOneToOneAssociation().isMandatory()) {
            options.add(CLEAR_ASSOCIATION);
        }
    }

    @Override
    public void setObject(final NakedObject object) {
        getOneToOneAssociation().setAssociation(getParent(), object);
    }

    public String title() {
        return adapter == null ? "" : adapter.titleString();
    }

    @Override
    public String toString() {
        return getObject() + "/" + getField();
    }

    @Override
    public String windowTitle() {
        return field.getName() + " for " + field.getParent().titleString();
    }

    public String getId() {
        return getOneToOneAssociation().getName();
    }

    public String getDescription() {
        final String name = getFieldName();
        String type = getField().getSpecification().getSingularName();
        type = name.indexOf(type) == -1 ? " (" + type + ")" : "";
        final String description = getOneToOneAssociation().getDescription();
        return name + type + " " + description;
    }

    public String getHelp() {
        return getOneToOneAssociation().getHelp();
    }
}
// Copyright (c) Naked Objects Group Ltd.
