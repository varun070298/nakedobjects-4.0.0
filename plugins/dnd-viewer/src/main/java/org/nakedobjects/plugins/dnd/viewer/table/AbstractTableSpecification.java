package org.nakedobjects.plugins.dnd.viewer.table;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;


public abstract class AbstractTableSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {
    private static final Logger LOG = Logger.getLogger(AbstractTableSpecification.class);

    private ViewSpecification rowSpecification;

    public AbstractTableSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
        rowSpecification = new TableRowSpecification();
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        if (!content.isCollection() || !requirement.is(ViewRequirement.OPEN)) {
            return false;
        } else {
            CollectionContent collectionContent = (CollectionContent) content;
            final NakedObjectSpecification elementSpecification = collectionContent.getElementSpecification();
            final NakedObjectAssociation[] fields = elementSpecification
                    .getAssociations(NakedObjectAssociationFilters.STATICALLY_VISIBLE_ASSOCIATIONS);
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isOneToOneAssociation()) {
                    return true;
                }
            }
            return false;
        }
    }

    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        return rowSpecification.createView(content, axis);
    }

    @Override
    protected ViewAxis axis(Content content) {
        // TODO create axis first, then after view built set up the axis details?
        final NakedObjectSpecification elementSpecification = ((CollectionContent) content).getElementSpecification();
        final NakedObjectAssociation[] accessibleFields = elementSpecification
                .getAssociations(NakedObjectAssociationFilters.STATICALLY_VISIBLE_ASSOCIATIONS);
        final TableAxis tableAxis = new TableAxis(tableFields(accessibleFields, content));
        // TODO make the setting of the column width strategy external so it can be changed
        tableAxis.setupColumnWidths(new TypeBasedColumnWidthStrategy());
        return tableAxis;
    }
    
    protected View decorateView(View view) {

   //     final View table = super.createView(content, tableAxis);
        TableAxis tableAxis = (TableAxis) view.getViewAxis();
        tableAxis.setRoot(view);

        return view;
        //return doCreateView(table, content, axis);
    }
    
    protected abstract View doCreateView(final View table, final Content content, final ViewAxis axis);

    public String getName() {
        return "Standard Table";
    }

    @Override
    public boolean isReplaceable() {
        return false;
    }

    @Override
    public boolean isResizeable() {
        return true;
    }
    
    private NakedObjectAssociation[] tableFields(final NakedObjectAssociation[] viewFields, final Content content) {
        for (int i = 0; i < viewFields.length; i++) {
            final NakedObjectAssociation nakedObjectAssociation = viewFields[i];
            // TODO reinstate check to skip unsuitable types
            /*
             * if (viewFields[i].getSpecification().isOfType(
             * NakedObjectsContext.getReflector().loadSpecification(ImageValue.class))) { continue; }
             */

          //  if (!nakedObjectAssociation.isVisible(NakedObjectsContext.getAuthenticationSession(), content.getNaked()).isAllowed()) {
          //      continue;
          //  }
            LOG.debug("column " + nakedObjectAssociation.getSpecification());
            // if(viewFields[i].getSpecification().isOfType(NakedObjects.getSpecificationLoader().lo));
        }

        final NakedObjectAssociation[] tableFields = new NakedObjectAssociation[viewFields.length];
        int c = 0;
        for (int i = 0; i < viewFields.length; i++) {
            if (!(viewFields[i] instanceof OneToManyAssociation)) {
                tableFields[c++] = viewFields[i];
            }
        }

        final NakedObjectAssociation[] results = new NakedObjectAssociation[c];
        System.arraycopy(tableFields, 0, results, 0, c);
        return results;
    }
}
// Copyright (c) Naked Objects Group Ltd.
