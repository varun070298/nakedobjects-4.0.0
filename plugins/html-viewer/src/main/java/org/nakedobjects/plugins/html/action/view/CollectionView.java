package org.nakedobjects.plugins.html.action.view;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.action.view.util.TableUtil;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.Table;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;



public class CollectionView implements Action {

    public final void execute(final Request request, final Context context, final Page page) {
        final String idString = request.getObjectId();
        final NakedObject collection = context.getMappedCollection(idString);
        if (collection == null) {
            throw new ActionException("No such collection: " + idString);
        }
        final String titleString = collection.titleString();

        page.setTitle(titleString);

        final TypeOfFacet facet = collection.getSpecification().getFacet(TypeOfFacet.class);
        final NakedObjectSpecification elementSpecification = facet.valueSpec();

        final ViewPane content = page.getViewPane();
        content.setWarningsAndMessages(context.getMessages(), context.getWarnings());
        content.setTitle(titleString, null);
        String iconName = collection.getIconName();
        if (iconName == null) {
            iconName = elementSpecification.getShortName();
        }
        content.setIconName(iconName);

        if (elementSpecification.getAssociations(NakedObjectAssociationFilters.STATICALLY_VISIBLE_ASSOCIATIONS).length != 0) {
            final Table table = TableUtil.createTable(context, false, collection, titleString, elementSpecification);
            content.add(table);
        } else {
            // TODO this should create a list component instead of a table
            final Table table = TableUtil.createTable(context, false, collection, titleString, elementSpecification);
            content.add(table);
        }

        context.addCollectionCrumb(idString);

        context.addCollectionToHistory(idString);
        context.clearMessagesAndWarnings();
    }

    public String name() {
        return Request.COLLECTION_COMMAND;
    }

}

// Copyright (c) Naked Objects Group Ltd.
