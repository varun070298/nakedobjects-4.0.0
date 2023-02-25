package org.nakedobjects.plugins.html.task;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;



public class AddItemToCollectionTask extends Task {
    private final OneToManyAssociation field;

    public AddItemToCollectionTask(final Context context, final NakedObject target, final OneToManyAssociation fld) {
        super(context, "Add to collection", "", target, 1);
        names[0] = fld.getName();
        descriptions[0] = fld.getDescription();
        fieldSpecifications[0] = fld.getSpecification();
        initialState[0] = null;
        optional[0] = true;
        // TODO add defaults and options
        this.field = fld;
    }

    @Override
    public void checkForValidity(final Context context) {
        final NakedObject target = getTarget(context);
        final NakedObject[] parameters = getEntries(context);

        final Consent valueValid = field.isValidToAdd(target, parameters[0]);
        errors[0] = valueValid.getReason();
    }

    @Override
    public void checkInstances(final Context context, final NakedObject[] objects) {
        final NakedObject collection = field.get(getTarget(context));
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final NakedObject target = getTarget(context);
        for (int i = 0; i < objects.length; i++) {
            final Consent valueValid = field.isValidToAdd(target, objects[i]);
            if (valueValid.isVetoed()) {
                objects[i] = null;
            } else if (facet.contains(collection, objects[i])) {
                objects[i] = null;
            }
        }
    }

    @Override
    public NakedObject completeTask(final Context context, final Page page) {
        final NakedObject targetAdapter = getTarget(context);
        final NakedObject[] parameterAdapters = getEntries(context);
        field.addElement(targetAdapter, parameterAdapters[0]);

        if (targetAdapter.isTransient()) {
            getPersistenceSession().makePersistent(targetAdapter);
        }
        return targetAdapter;
    }

    @Override
    public boolean isEditing() {
        return true;
    }

    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }


}

// Copyright (c) Naked Objects Group Ltd.
