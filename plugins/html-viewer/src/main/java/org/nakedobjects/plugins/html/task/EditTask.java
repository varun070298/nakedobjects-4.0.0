package org.nakedobjects.plugins.html.task;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.util.NakedObjectUtils;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;


public class EditTask extends Task {
    private static int size(final NakedObject object) {
        final NakedObjectAssociation[] fields = object.getSpecification().getAssociations(
                NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));
        return fields.length;
    }

    private static boolean skipField(final NakedObject object, final NakedObjectAssociation fld) {
        return fld.isOneToManyAssociation() || fld.isUsable(NakedObjectsContext.getAuthenticationSession(), object).isVetoed();
    }

    private final NakedObjectAssociation[] fields;
    private final String newType;

    public EditTask(final Context context, final NakedObject object) {
        super(context, "Edit", "", object, size(object));

        final NakedObjectAssociation[] allFields = object.getSpecification().getAssociations(
                NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));

        fields = new NakedObjectAssociation[names.length];
        for (int i = 0, j = 0; j < allFields.length; j++) {
            final NakedObjectAssociation fld = allFields[j];
            fields[i] = fld;
            names[i] = fld.getName();
            descriptions[i] = fld.getDescription();

            final Consent usableByUser = fld.isUsable(NakedObjectsContext.getAuthenticationSession(), object);
            if (usableByUser.isVetoed()) {
                descriptions[i] = usableByUser.getReason();
            }

            fieldSpecifications[i] = fld.getSpecification();
            initialState[i] = fld.get(object);
            if (skipField(object, fld)) {
                readOnly[i] = true;
            } else {
                readOnly[i] = false;
                optional[i] = !fld.isMandatory();
                if (fieldSpecifications[i].isParseable()) {
                    final MultiLineFacet multilineFacet = fld.getFacet(MultiLineFacet.class);
                    noLines[i] = multilineFacet.numberOfLines();
                    wraps[i] = !multilineFacet.preventWrapping();

                    final MaxLengthFacet maxLengthFacet = fld.getFacet(MaxLengthFacet.class);
                    maxLength[i] = maxLengthFacet.value();

                    final TypicalLengthFacet typicalLengthFacet = fld.getFacet(TypicalLengthFacet.class);
                    typicalLength[i] = typicalLengthFacet.value();
                }
            }
            i++;
        }

        final boolean isNew = object.getResolveState() == ResolveState.TRANSIENT;
        newType = isNew ? getTarget(context).getSpecification().getSingularName() : null;
    }

    @Override
    protected NakedObject[][] getOptions(final Context context, final int from, final int len) {
        final NakedObject target = getTarget(context);
        final NakedObject[][] options = new NakedObject[len][];
        for (int i = from, j = 0; j < len; i++, j++) {
            if (skipField(target, fields[i])) {} else {
                options[j] = fields[i].getChoices(target);
            }
        }
        return options;
    }

    @Override
    public void checkForValidity(final Context context) {
        final NakedObject target = getTarget(context);
        final NakedObject[] entries = getEntries(context);

        final int len = fields.length;
        for (int i = 0; i < len; i++) {
            if (readOnly[i] || errors[i] != null) {
                continue;
            }
            final NakedObjectAssociation fld = fields[i];
            if (fld.isOneToOneAssociation()) {
                final OneToOneAssociation oneToOneAssociation = (OneToOneAssociation) fld;
                final NakedObject entryReference = entries[i];
                final NakedObject currentReference = oneToOneAssociation.get(target);
                if (currentReference != entryReference) {
                    final Consent valueValid = ((OneToOneAssociation) fld).isAssociationValid(target, entryReference);
                    errors[i] = valueValid.getReason();
                }
            }
        }
        
        if (target.isTransient()) {
            saveState(target, entries);
            Consent isValid = target.getSpecification().isValid(target);
            error = isValid.isVetoed() ? isValid.getReason() : null;
        }
    }

    @Override
    public NakedObject completeTask(final Context context, final Page page) {
        final NakedObject targetAdapter = getTarget(context);
        final NakedObject[] entryAdapters = getEntries(context);

        if (targetAdapter.isTransient()) {
            final NakedObjectAction action = 
                targetAdapter.getSpecification().getObjectAction(NakedObjectActionConstants.USER, "save", new NakedObjectSpecification[0]);
            if (action == null) {
                getPersistenceSession().makePersistent(targetAdapter);
            } else {
                action.execute(targetAdapter, new NakedObject[0]);
            }
        } else {
            saveState(targetAdapter, entryAdapters);
        }

        return targetAdapter;
    }

    private void saveState(final NakedObject targetAdapter, final NakedObject[] entryAdapters) {
        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation fld = fields[i];
            final NakedObject entryAdapter = entryAdapters[i];
            final boolean isReadOnly = readOnly[i];
            
            if (isReadOnly) {
            	continue;
            }
            
            if (fld.isOneToOneAssociation()) {
                final OneToOneAssociation oneToOneAssociation = ((OneToOneAssociation) fld);
                final Object entryPojo = NakedObjectUtils.unwrap(entryAdapter);
                if (entryPojo == null) {
                    if (oneToOneAssociation.get(targetAdapter) != null) {
                        oneToOneAssociation.clearAssociation(targetAdapter);
                    }
                } else {
                    final NakedObject currentAdapter = oneToOneAssociation.get(targetAdapter);
                    final Object currentPojo = NakedObjectUtils.unwrap(currentAdapter);
                    if (currentAdapter == null || currentPojo == null || !currentPojo.equals(entryPojo)) {
                        oneToOneAssociation.setAssociation(targetAdapter, entryAdapter);
                    }
                }
            }
        }
    }

    @Override
    protected boolean simpleField(final NakedObjectSpecification type, final int i) {
        return !fields[i].hasChoices() || super.simpleField(type, i);
    }

    @Override
    public boolean isEditing() {
        return true;
    }

    @Override
    public String getName() {
        if (newType == null) {
            return super.getName();
        } else {
            return "New " + newType;
        }
    }
    
    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////
 
    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }


}

// Copyright (c) Naked Objects Group Ltd.
