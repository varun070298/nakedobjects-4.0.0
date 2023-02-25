package org.nakedobjects.plugins.dnd.viewer.basic;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.content.PerspectiveContent;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;


/**
 * WorkspaceBuilder builds a workspace view for an ObjectContent view by finding a collection of classes from
 * a field called 'classes' and adding an icon for each element. Similarly, if there is a collection called
 * 'objects' its elements are also added to the display.
 * 
 * <p>
 * During lay-out any icons that have an UNPLACED location (-1, -1) are given a location. Objects of type
 * NakedClass are added to the left-hand side, while all other icons are placed on the right-hand side of the
 * workspace view. Open windows are displayed in the centre.
 */
public class ApplicationWorkspaceBuilder extends AbstractViewBuilder {
    private static final Logger LOG = Logger.getLogger(ApplicationWorkspaceBuilder.class);
    private static final int PADDING = 10;
    public static final Location UNPLACED = new Location(-1, -1);

    @Override
    public void build(final View view1) {
        final ApplicationWorkspace workspace = (ApplicationWorkspace) view1;

        PerspectiveContent perspectiveContent = (PerspectiveContent) view1.getContent();
        
        // REVIEW is this needed?
        workspace.clearServiceViews();

        PerspectiveEntry perspective = perspectiveContent.getPerspective();
        for (Object object : perspective.getObjects()) {
            NakedObject adapter = NakedObjectsContext.getPersistenceSession().getAdapterManager().adapterFor(object);
            workspace.addIconFor(adapter, ApplicationWorkspaceBuilder.UNPLACED);
        }
        
        for (Object service : perspective.getServices()) {
            NakedObject adapter = NakedObjectsContext.getPersistenceSession().getAdapterManager().adapterFor(service);
            if (isHidden(adapter)) {
                continue;
            }
            workspace.addServiceIconFor(adapter);
        }
    }

    private boolean isHidden(NakedObject serviceNO) {
        NakedObjectSpecification serviceNoSpec = serviceNO.getSpecification();
        return serviceNoSpec.getFacet(HiddenFacet.class) != null;
    }

    public boolean canDisplay(final NakedObject object) {
        return object instanceof NakedObject && object != null;
    }

    @Override
    public Size getRequiredSize(final View view) {
        return new Size(600, 400);
    }

    public String getName() {
        return "Simple Workspace";
    }

    @Override
    public void layout(final View view1, final Size maximumSize) {
        final ApplicationWorkspace view = (ApplicationWorkspace) view1;

        final int widthUsed = layoutServiceIcons(maximumSize, view);
        layoutObjectIcons(maximumSize, view);
        layoutWindowViews(maximumSize, view, widthUsed);
    }

    private void layoutWindowViews(final Size maximumSize, final ApplicationWorkspace view, final int xOffset) {
        final Size size = view.getSize();
        size.contract(view.getPadding());

        final int maxHeight = size.getHeight();
        final int maxWidth = size.getWidth();

        final int xWindow = xOffset + PADDING;
        int yWindow = PADDING;

        int xMinimized = 1;
        int yMinimized = maxHeight - 1;

        final View views[] = view.getWindowViews();

        for (int i = 0; i < views.length; i++) {
            final View subview = views[i];
            subview.layout(new Size(maximumSize));
        }

        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size componentSize = v.getRequiredSize(new Size(size));
            v.setSize(componentSize);
            if (v instanceof MinimizedView) {
                final Size s = v.getMaximumSize();
                if (xMinimized + s.getWidth() > maxWidth) {
                    xMinimized = 1;
                    yMinimized -= s.getHeight() + 1;
                }
                v.setLocation(new Location(xMinimized, yMinimized - s.getHeight()));
                xMinimized += s.getWidth() + 1;

            } else if (v.getLocation().equals(UNPLACED)) {
                final int height = componentSize.getHeight() + 6;
                v.setLocation(new Location(xWindow, yWindow));
                yWindow += height;

            }
            v.limitBoundsWithin(maximumSize);
        }
    }

    private int layoutServiceIcons(final Size maximumSize, final ApplicationWorkspace view) {
        final Size size = view.getSize();
        size.contract(view.getPadding());

        final int maxHeight = size.getHeight();

        int xService = PADDING;
        int yService = PADDING;
        int maxServiceWidth = 0;

        final View views[] = view.getServiceIconViews();
        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size componentSize = v.getRequiredSize(new Size(size));
            v.setSize(componentSize);
            final int height = componentSize.getHeight() + 6;

            final NakedObject object = v.getContent().getNaked();
            if (object.getSpecification().isService()) {
                if (yService + height > maxHeight) {
                    yService = PADDING;
                    xService += maxServiceWidth + PADDING;
                    maxServiceWidth = 0;
                    LOG.debug("creating new column at " + xService + ", " + yService);
                }
                LOG.debug("service icon at " + xService + ", " + yService);
                v.setLocation(new Location(xService, yService));
                maxServiceWidth = Math.max(maxServiceWidth, componentSize.getWidth());
                yService += height;
            }
            v.limitBoundsWithin(maximumSize);
        }

        return xService + maxServiceWidth;
    }

    private void layoutObjectIcons(final Size maximumSize, final ApplicationWorkspace view) {
        final Size size = view.getSize();
        size.contract(view.getPadding());

        final int maxWidth = size.getWidth();

        final int xObject = maxWidth - PADDING;
        int yObject = PADDING;

        final View views[] = view.getObjectIconViews();
        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size componentSize = v.getRequiredSize(new Size(size));
            v.setSize(componentSize);
            if (v.getLocation().equals(UNPLACED)) {
                final int height = componentSize.getHeight() + 6;
                v.setLocation(new Location(xObject - componentSize.getWidth(), yObject));
                yObject += height;
            }
            v.limitBoundsWithin(maximumSize);
        }
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        throw new NotYetImplementedException();
    }

}
// Copyright (c) Naked Objects Group Ltd.
