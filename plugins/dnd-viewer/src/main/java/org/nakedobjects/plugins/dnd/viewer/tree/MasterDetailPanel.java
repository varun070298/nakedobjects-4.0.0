package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.border.SelectableViewAxis;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.list.SimpleListSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.form.InternalFormWithoutCollectionsSpecification;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractCompositeView;
import org.nakedobjects.plugins.dnd.viewer.view.simple.BlankView;

public class MasterDetailPanel extends AbstractCompositeView {
    private static final int MINIMUM_WIDTH = 120;
    private final ViewSpecification mainViewFormSpec;
    private final ViewSpecification mainViewListSpec;
    private final ViewSpecification mainViewTableSpec;
    private final ViewSpecification rightHandSideSpecification;
    
    public MasterDetailPanel(Content content, ViewSpecification specification, ViewAxis axis, ViewSpecification rightHandSideSpecification) {
        super(content, specification, axis);
        this.rightHandSideSpecification = rightHandSideSpecification;  

        mainViewFormSpec = new InternalFormWithoutCollectionsSpecification();
        mainViewTableSpec = new InternalTableSpecification();
        mainViewListSpec = new SimpleListSpecification();
    }
    
    protected void buildView() {
        SelectableViewAxis axis = new SelectableViewAxis(this);
        
        Content content = getContent();
        View listView = rightHandSideSpecification.createView(content, axis);
        listView = new ViewResizeBorder(new ScrollBorder(listView));
        listView.setParent(getView());
        addView(listView);
        
        View blankView = new BlankView(new NullContent());
        blankView.setParent(getView());
        addView(blankView);    
        
        if (content instanceof CollectionContent) {
            NakedObject firstElement = ((CollectionContent) content).elements()[0];
            final Content firstElementContent = Toolkit.getContentFactory().createRootContent(firstElement);
            setSelectedNode(firstElementContent);
        } else if (content instanceof ObjectContent) {
            /*
             * TODO provide a view that shows first useful object (not redisplaying parent)
             * 
            NakedObjectAssociation[] associations = content.getSpecification().getAssociations();
            for (int i = 0; i < associations.length; i++) {
                NakedObjectAssociation assoc = associations[i];
                if (assoc.isOneToManyAssociation()) {
                    NakedObject collection = assoc.get(content.getNaked());
                    final Content collectionContent = Toolkit.getContentFactory().createRootContent(collection);
                    setSelectedNode(collectionContent);
                    break;
                } else   if (assoc.isOneToOneAssociation() && !((OneToOneAssociation)assoc).getSpecification().isParseable()) {
                    NakedObject object = assoc.get(content.getNaked());
                    if (object == null) {
                        continue;
                    }
                    final Content objectContent = Toolkit.getContentFactory().createRootContent(object);
                    setSelectedNode(objectContent);
                    break;
                }
            }
            */
            setSelectedNode(content);
        }
    }

    protected void doLayout(Size maximumSize) {
        maximumSize.contract(getView().getPadding());

        View[] subviews = getSubviews();
        View left = subviews[0];
        View right = subviews[1];
        Size leftPanelRequiredSize = left.getRequiredSize(new Size(maximumSize));
        Size rightPanelRequiredSize = right == null ? new Size() : right.getRequiredSize(new Size(maximumSize));

        if (leftPanelRequiredSize.getWidth() + rightPanelRequiredSize.getWidth() > maximumSize.getWidth()) {
            /*
             * If the combined width is greater than the available then we need to divide the space between
             * the two sides and recalculate
             */
            final int availableWidth = maximumSize.getWidth();
            final int leftWidth = Math.max(MINIMUM_WIDTH, leftPanelRequiredSize.getWidth());
            final int rightWidth = rightPanelRequiredSize.getWidth();
            final int totalWidth = leftWidth + rightWidth;

            final int bestWidth = (int) (1.0 * leftWidth / totalWidth * availableWidth);
            final Size maximumSizeLeft = new Size(bestWidth, maximumSize.getHeight());
            leftPanelRequiredSize = left.getRequiredSize(maximumSizeLeft);

            final Size maximumSizeRight = new Size(availableWidth - leftPanelRequiredSize.getWidth(), maximumSize.getHeight());
            rightPanelRequiredSize = right.getRequiredSize(maximumSizeRight);
        }

        // combine the two sizes
        final Size combinedSize = new Size(leftPanelRequiredSize);
        combinedSize.extendWidth(rightPanelRequiredSize.getWidth());
        combinedSize.ensureHeight(rightPanelRequiredSize.getHeight());
        combinedSize.setHeight(Math.min(combinedSize.getHeight(), maximumSize.getHeight()));

        left.setSize(new Size(leftPanelRequiredSize.getWidth(), combinedSize.getHeight()));
        left.layout(new Size(new Size(leftPanelRequiredSize)));

        if (right != null) {
            right.setLocation(new Location(left.getSize().getWidth(), 0));
            rightPanelRequiredSize.setHeight(combinedSize.getHeight());

            right.setSize(rightPanelRequiredSize);
            right.layout(rightPanelRequiredSize);
        }
    }

    @Override
    public Size getMaximumSize() {
        final Size total = new Size();
        View[] subviews = getSubviews();
        for (View view : subviews) {
            Size size = view.getSize();
            total.extendWidth(size.getWidth());
            total.ensureHeight(size.getHeight());
        }
        return total;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        View[] subviews = getSubviews();
        View left = subviews[0];
        View right = subviews.length > 1 ? subviews[1] : null;
        
        Size leftPanelRequiredSize = left.getRequiredSize(new Size(maximumSize));
        Size rightPanelRequiredSize = right == null ? new Size() : right.getRequiredSize(new Size(maximumSize));

        if (leftPanelRequiredSize.getWidth() + rightPanelRequiredSize.getWidth() > maximumSize.getWidth()) {
            /*
             * If the combined width is greater than the available then we need to divide the space between the
             * two sides and recalculate
             */
            
            final int availableWidth = maximumSize.getWidth();
            final int leftWidth = leftPanelRequiredSize.getWidth();
            final int rightWidth = Math.max(MINIMUM_WIDTH, rightPanelRequiredSize.getWidth());
            final int totalWidth = leftWidth + rightWidth;

            final int bestWidth = (int) (1.0 * leftWidth / totalWidth * availableWidth);
            final Size maximumSizeLeft = new Size(bestWidth, maximumSize.getHeight());
            leftPanelRequiredSize = left.getRequiredSize(maximumSizeLeft);

            final Size maximumSizeRight = new Size(availableWidth - leftPanelRequiredSize.getWidth(), maximumSize.getHeight());
            rightPanelRequiredSize = right.getRequiredSize(maximumSizeRight);
        }

        // combine the two required sizes
        final Size combinedSize = new Size(leftPanelRequiredSize);
        combinedSize.extendWidth(rightPanelRequiredSize.getWidth());
        combinedSize.ensureHeight(rightPanelRequiredSize.getHeight());
        return combinedSize;
    }
    
    protected void showInRightPane(final View view) {
        replaceView(getSubviews()[1], view);
    }

    public void setSelectedNode(final View view) {
        final Content content = view.getContent();
        setSelectedNode(content);
    }

    private void setSelectedNode(final Content content) {
        final NakedObject object = content.getNaked();
        final NakedObjectSpecification specification = object.getSpecification();
        final CollectionFacet facet = specification.getFacet(CollectionFacet.class);
        ViewRequirement requirement = new ViewRequirement(content, ViewRequirement.OPEN);
        if (facet != null && facet.size(object) > 0) {
            if (mainViewTableSpec.canDisplay(content, requirement)) {
                showInRightPane(mainViewTableSpec.createView(content, null));
            } else if (mainViewListSpec.canDisplay(content, requirement)) {
                showInRightPane(mainViewListSpec.createView(content, null));
            }

        } else if (specification.isObject()) {
            if (object != null && mainViewFormSpec.canDisplay(content, requirement)) {
                showInRightPane(mainViewFormSpec.createView(content, null));
            }
        }
    }

    @Override
    public String toString() {
        return "MasterDetailPanel" + getId();
    }

}


// Copyright (c) Naked Objects Group Ltd.
