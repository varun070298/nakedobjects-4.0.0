package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableField;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class TextParseableFieldImpl extends AbstractTextParsableContent implements TextParseableField {
    private final ObjectField field;
    private final NakedObject parent;
    private NakedObject object;

    public TextParseableFieldImpl(final NakedObject parent, final NakedObject object, final OneToOneAssociation association) {
        field = new ObjectField(parent, association);
        this.parent = parent;
        this.object = object;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public boolean canClear() {
        return true; // TODO is this flagged anywhere - getValueAssociation().canClear();
    }

    public boolean canWrap() {
        return getValueAssociation().containsFacet(MultiLineFacet.class);
    }

    @Override
    public void clear() {}

    public void debugDetails(final DebugString debug) {
        field.debugDetails(debug);
        debug.appendln("object", object);
    }

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    @Override
    public void entryComplete() {
        getValueAssociation().setAssociation(getParent(), object);
    }

    public String getDescription() {
        final String title = object == null ? "" : ": " + object.titleString();
        final String name = field.getName();
        final NakedObjectSpecification specification = getSpecification();
        final String type = name.indexOf(specification.getShortName()) == -1 ? "" : " (" + specification.getShortName() + ")";
        final String description = getValueAssociation().getDescription();
        return name + type + title + " " + description;
    }

    public String getHelp() {
        return field.getHelp();
    }

    public String getFieldName() {
        return field.getName();
    }

    public NakedObjectAssociation getField() {
        return field.getNakedObjectAssociation();
    }

    public String getIconName() {
        return object == null ? "" : object.getIconName();
    }

    public NakedObject getNaked() {
        return object;
    }

    public String getId() {
        return field.getName();
    }

    public NakedObject[] getOptions() {
        return getValueAssociation().getChoices(getParent());
    }

    private OneToOneAssociation getValueAssociation() {
        return (OneToOneAssociation) getField();
    }

    public int getMaximumLength() {
        return maxLengthFacet().value();
    }

    public int getTypicalLineLength() {
        final TypicalLengthFacet facet = field.getNakedObjectAssociation().getFacet(TypicalLengthFacet.class);
        return facet.value();
    }

    public int getNoLines() {
        return multilineFacet().numberOfLines();
    }

    public NakedObject getParent() {
        return field.getParent();
    }

    public NakedObjectSpecification getSpecification() {
        return getValueAssociation().getSpecification();
    }

    @Override
    public Consent isEditable() {
        return getValueAssociation().isUsable(NakedObjectsContext.getAuthenticationSession(), getParent());
    }

    @Override
    public boolean isEmpty() {
        return getField().isEmpty(getParent());
    }

    public boolean isMandatory() {
        return getValueAssociation().isMandatory();
    }

    public boolean isOptionEnabled() {
        return getValueAssociation().hasChoices();
    }

    public String titleString(final NakedObject value) {
        return titleString(value, field.getNakedObjectAssociation(), field.getSpecification());
    }

    private NakedObject validateAndParse(final String entryText) {
        final NakedObject newValue = parse(entryText);
        final OneToOneAssociation nakedObjectAssociation = (OneToOneAssociation) field.getNakedObjectAssociation();
        final Consent valid = nakedObjectAssociation.isAssociationValid(parent, newValue);
        if (valid.isVetoed()) {
            throw new InvalidEntryException(valid.getReason());
        }
        return newValue;
    }

    private NakedObject parse(final String entryText) {
        final NakedObjectSpecification fieldSpecification = field.getSpecification();
        final ParseableFacet p = fieldSpecification.getFacet(ParseableFacet.class);
        try {
        	return p.parseTextEntry(object, entryText);
        } catch(IllegalArgumentException ex) {
        	throw new InvalidEntryException(ex.getMessage(), ex);
        }
    }

    @Override
    public void parseTextEntry(final String entryText) {
        object = validateAndParse(entryText);
        final Consent valid = ((OneToOneAssociation) getField()).isAssociationValid(getParent(), object);
        if (valid.isVetoed()) {
            throw new InvalidEntryException(valid.getReason());
        }
        if (getValueAssociation().isMandatory() && object == null) {
            throw new InvalidEntryException("Mandatory field cannot be empty");
        }
    }

    public String title() {
        return field.getName();
    }

    @Override
    public String toString() {
        return (object == null ? "null" : object.titleString()) + "/" + getField();
    }

    @Override
    public String windowTitle() {
        return title();
    }

    private MaxLengthFacet maxLengthFacet() {
        return getValueAssociation().getFacet(MaxLengthFacet.class);
    }

    private MultiLineFacet multilineFacet() {
        return getValueAssociation().getFacet(MultiLineFacet.class);
    }

}
// Copyright (c) Naked Objects Group Ltd.
