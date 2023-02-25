package org.nakedobjects.plugins.html.action.view;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.html.action.view.util.TableUtil;
import org.nakedobjects.plugins.html.component.Table;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public class FieldCollectionView extends ObjectViewAbstract {
    @Override
    protected void doExecute(final Context context, final ViewPane content, final NakedObject object, final String field) {
        final String id = context.mapObject(object);
        final NakedObjectSpecification specification = object.getSpecification();

        final OneToManyAssociation collection = (OneToManyAssociation) specification.getAssociation(field);
        
        NakedObjectsContext.getPersistenceSession().resolveField(object, collection);
        
        context.addCollectionFieldCrumb(collection.getName());
        content.add(context.getComponentFactory().createHeading(collection.getName()));
        final Table table = TableUtil.createTable(context, id, object, collection);
        content.add(table);
        if (collection.isUsable(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
            content.add(context.getComponentFactory().createAddOption(id, collection.getId()));
        }
    }

    public String name() {
        return Request.FIELD_COLLECTION_COMMAND;
    }

}

// Copyright (c) Naked Objects Group Ltd.
