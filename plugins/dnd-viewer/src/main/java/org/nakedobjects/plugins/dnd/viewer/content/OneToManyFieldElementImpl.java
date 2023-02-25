package org.nakedobjects.plugins.dnd.viewer.content;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.dnd.OneToManyFieldElement;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class OneToManyFieldElementImpl extends AbstractObjectContent implements OneToManyFieldElement {
    private static final Logger LOG = Logger.getLogger(OneToManyFieldElementImpl.class);
    private final NakedObject element;
    private final ObjectField field;

    public OneToManyFieldElementImpl(final NakedObject parent, final NakedObject element, final OneToManyAssociation association) {
        field = new ObjectField(parent, association);
        this.element = element;
    }

    @Override
    public Consent canClear() {
        final NakedObject parentObject = getParent();
        final OneToManyAssociation association = getOneToManyAssociation();
        final NakedObject associatedObject = getObject();

        final Consent isEditable = isEditable();
        if (isEditable.isVetoed()) {
            return isEditable;
        }

        final Consent consent = association.isValidToRemove(parentObject, associatedObject);
        if (consent.isAllowed()) {
            consent.setDescription("Clear the association to this object from '" + parentObject.titleString() + "'");
        }
        return consent;
    }

    public Consent isEditable() {
        return getField().isUsable(NakedObjectsContext.getAuthenticationSession(), getParent());
    }

    @Override
    public Consent canSet(final NakedObject dragSource) {
        return Veto.DEFAULT;
    }

    @Override
    public void clear() {
        final NakedObject parentObject = getParent();
        final OneToManyAssociation association = getOneToManyAssociation();
        LOG.debug("remove " + element + " from " + parentObject);
        association.removeElement(parentObject, element);
    }

    public void debugDetails(final DebugString debug) {
        field.debugDetails(debug);
        debug.appendln("element", element);
    }

    public String getFieldName() {
        return field.getName();
    }

    public NakedObjectAssociation getField() {
        return field.getNakedObjectAssociation();
    }

    public NakedObject getNaked() {
        return element;
    }

    @Override
    public NakedObject getObject() {
        return element;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    private OneToManyAssociation getOneToManyAssociation() {
        return (OneToManyAssociation) field.getNakedObjectAssociation();
    }

    public NakedObject getParent() {
        return field.getParent();
    }

    public NakedObjectSpecification getSpecification() {
        return field.getSpecification();
    }

    public boolean isMandatory() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        // ObjectOption.menuOptions(element, options);
        super.contentMenuOptions(options);
        options.add(new ClearOneToManyAssociationOption());
    }

    @Override
    public void setObject(final NakedObject object) {
    /*
     * NakedObject parentObject = getParent(); OneToManyAssociationSpecification association =
     * getOneToManyAssociation(); NakedObject associatedObject = getObject(); LOG.debug("remove " +
     * associatedObject + " from " + parentObject); association.clearAssociation(parentObject,
     * associatedObject);
     */

    }

    public String title() {
        return element.titleString();
    }

    @Override
    public String toString() {
        return getObject() + "/" + field.getNakedObjectAssociation();
    }

    @Override
    public String windowTitle() {
        return field.getName() + " element" + " for " + field.getParent().titleString();
    }

    public String getId() {
        return getOneToManyAssociation().getName();
    }

    public String getDescription() {
        return field.getName() + ": " + getOneToManyAssociation().getDescription();
    }

    public String getHelp() {
        return getOneToManyAssociation().getHelp();
    }
}
// Copyright (c) Naked Objects Group Ltd.
