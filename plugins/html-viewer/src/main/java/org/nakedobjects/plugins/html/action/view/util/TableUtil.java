package org.nakedobjects.plugins.html.action.view.util;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.html.component.ComponentFactory;
import org.nakedobjects.plugins.html.component.Table;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;

import static org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters.PROPERTIES;
import static org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters.STATICALLY_VISIBLE_ASSOCIATIONS;



public class TableUtil {

    public static Table createTable(
            final Context context,
            final String id,
            final NakedObject object,
            final OneToManyAssociation collectionField) {

        final NakedObject collection = collectionField.get(object);
        final String name = collectionField.getName();
        final NakedObjectSpecification type = collectionField.getSpecification();

        final String summary = "Table showing elements of " + name + " field";
        return createTable(context, collectionField != null, collection, summary, type);
    }

    public static Table createTable(
            final Context context,
            final boolean addSelector,
            final NakedObject collection,
            final String summary,
            final NakedObjectSpecification elementType) {

    	final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final NakedObjectAssociation[] columnAssociations = 
        	elementType.getAssociations(STATICALLY_VISIBLE_ASSOCIATIONS.and(PROPERTIES));
        
        int len = columnAssociations.length;

        ComponentFactory factory = context.getComponentFactory();
        final Table table = factory.createTable(len, addSelector);
        table.setSummary(summary);

        for (NakedObjectAssociation columnAssociation : columnAssociations) {
        	table.addColumnHeader(columnAssociation.getName());
		}

        for(NakedObject rowAdapter: facet.iterable(collection)) {
		    getPersistenceSession().resolveImmediately(rowAdapter);
		    final String elementId = context.mapObject(rowAdapter);
		    table.addRowHeader(factory.createObjectIcon(rowAdapter, elementId, "icon"));
		    
	        for (NakedObjectAssociation columnAssociation : columnAssociations) {
		        final NakedObject columnAdapter = columnAssociation.get(rowAdapter);
		
		        NakedObjectSpecification columnSpec = columnAssociation.getSpecification();
				if (!columnAssociation.isVisible(getAuthenticationSession(), rowAdapter).isAllowed()) {
		            table.addEmptyCell();
		        } else if (columnSpec.isParseable()) {
		            final MultiLineFacet multiline = columnSpec.getFacet(MultiLineFacet.class);
		            final boolean shouldTruncate = multiline != null && multiline.numberOfLines() > 1;
		            final String titleString = columnAdapter != null ? columnAdapter.titleString() : "";
		            table.addCell(titleString, shouldTruncate);
		        } else if (columnAdapter == null) {
		            table.addEmptyCell();
		        } else {
		            getPersistenceSession().resolveImmediately(columnAdapter);	            
		            final String objectId = context.mapObject(columnAdapter);
                    table.addCell(factory.createObjectIcon(columnAssociation, columnAdapter, objectId, "icon"));
                }
		    }
		    /*
		     * if (addSelector) { table.addCell(context.getFactory().createRemoveOption(id, elementId,
		     * collectionField.getId())); }
		     */
		    // TODO add selection box
		    // table.addCell();
		    /*
		     * if (collectionField != null) { if (collectionField.isValidToRemove(object,
		     * element).isAllowed()) { table.addCell(context.getFactory().createRemoveOption(id, elementId,
		     * collectionField.getId())); } else { table.addEmptyCell(); } }
		     */
		
		}
        return table;
    }

    
    //////////////////////////////////////////////////////////////////////////////////
    // Dependencies (from context)
    //////////////////////////////////////////////////////////////////////////////////
    
	private static PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}

	private static AuthenticationSession getAuthenticationSession() {
		return NakedObjectsContext.getAuthenticationSession();
	}

}

// Copyright (c) Naked Objects Group Ltd.
