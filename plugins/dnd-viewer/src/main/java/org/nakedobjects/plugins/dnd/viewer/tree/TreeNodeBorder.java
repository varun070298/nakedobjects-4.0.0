package org.nakedobjects.plugins.dnd.viewer.tree;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.OneToManyField;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dnd.viewer.border.SelectableViewAxis;
import org.nakedobjects.plugins.dnd.viewer.content.CollectionElement;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.DragViewOutline;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;
import org.nakedobjects.runtime.context.NakedObjectsContext;


// TODO use ObjectBorder to provide the basic border functionality
public class TreeNodeBorder extends AbstractBorder {
    private static final int BORDER = 13;
    private static final int BOX_PADDING = 2;
    private static final int BOX_SIZE = 9;
    private static final int BOX_X_OFFSET = 5;
    private final static Text LABEL_STYLE = Toolkit.getText(ColorsAndFonts.TEXT_NORMAL);
    private static final Logger LOG = Logger.getLogger(TreeNodeBorder.class);
    private final int baseline;
    private final IconGraphic icon;
    private final ViewSpecification replaceWithSpecification;
    private final TitleText text;

    public TreeNodeBorder(final View wrappedView, final ViewSpecification replaceWith) {
        super(wrappedView);

        replaceWithSpecification = replaceWith;

        icon = new IconGraphic(this, LABEL_STYLE);
        text = new ObjectTitleText(this, LABEL_STYLE);
        final int height = icon.getSize().getHeight();

        baseline = icon.getBaseline() + 1;

        left = 22;
        right = 0 + BORDER;
        top = height + 2;
        bottom = 0;
    }

    private int canOpen() {
        return ((NodeSpecification) getSpecification()).canOpen(getContent());
    }

    @Override
    protected Bounds contentArea() {
        return new Bounds(getLeft(), getTop(), wrappedView.getSize().getWidth(), wrappedView.getSize().getHeight());
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.append("TreeNodeBorder " + left + " pixels\n");
        debug.append("           titlebar " + (top) + " pixels\n");
        debug.append("           replace with  " + replaceWithSpecification);
        debug.append("           text " + text);
        debug.append("           icon " + icon);
        super.debugDetails(debug);

    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (drag.getLocation().getX() > getSize().getWidth() - right) {
            final View dragOverlay = new DragViewOutline(getView());
            return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
        } else if (overBorder(drag.getLocation())) {
            final View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
            return new ContentDrag(this, drag.getLocation(), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        Color secondary1 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        Color secondary2 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        Color secondary3 = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3);
        if (getViewAxis() instanceof SelectableViewAxis) {
            if (((SelectableViewAxis) getViewAxis()).isSelected(getView())) {
                canvas.drawSolidRectangle(left, 0, getSize().getWidth() - left, top, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2));
                secondary2 = secondary1;
            }
        }

        if (getState().isObjectIdentified()) {
            canvas.drawRectangle(left, 0, getSize().getWidth() - left, top, secondary2);

            final int xExtent = getSize().getWidth();
            canvas.drawSolidRectangle(xExtent - BORDER + 1, 1, BORDER - 2, top - 2, secondary3);
            canvas.drawLine(xExtent - BORDER, 0, xExtent - BORDER, top - 2, secondary2);
        }

        // lines
        int x = 0;
        final int y = top / 2;
        canvas.drawLine(x, y, x + left, y, secondary2);

        boolean isOpen = getSpecification().isOpen();
        final int canOpen = canOpen();
        final boolean addBox = isOpen || canOpen != NodeSpecification.CANT_OPEN;
        if (addBox) {
            x += BOX_X_OFFSET;
            canvas.drawLine(x, y, x + BOX_SIZE - 1, y, secondary3);
            canvas.drawSolidRectangle(x, y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
            canvas.drawRectangle(x, y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE, secondary1);

            if (canOpen == NodeSpecification.UNKNOWN) {

            } else {
                Color black = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
                canvas.drawLine(x + BOX_PADDING, y, x + BOX_SIZE - 1 - BOX_PADDING, y, black);
                if (!isOpen) {
                    x += BOX_SIZE / 2;
                    canvas.drawLine(x, y - BOX_SIZE / 2 + BOX_PADDING, x, y + BOX_SIZE / 2 - BOX_PADDING, black);
                }
            }
        }

        final View[] nodes = getSubviews();
        if (nodes.length > 0) {
            final int y1 = top / 2;
            final View node = nodes[nodes.length - 1];
            final int y2 = top + node.getLocation().getY() + top / 2;
            canvas.drawLine(left - 1, y1, left - 1, y2, secondary2);
        }

        // icon & title
        x = left + 1;
        icon.draw(canvas, x, baseline);
        x += icon.getSize().getWidth();
        text.draw(canvas, x, baseline);

        if (Toolkit.debug) {
            canvas.drawRectangleAround(this, Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BASELINE));
        }

        // draw components
        super.draw(canvas);
    }

    @Override
    public void entered() {
        getState().setContentIdentified();
        getState().setViewIdentified();
        wrappedView.entered();
        markDamaged();
    }

    @Override
    public void exited() {
        getState().clearObjectIdentified();
        getState().clearViewIdentified();
        wrappedView.exited();
        markDamaged();
    }

    @Override
    public void firstClick(final Click click) {
        final int x = click.getLocation().getX();
        final int y = click.getLocation().getY();

        if (withinBox(x, y)) {
            if (canOpen() == NodeSpecification.UNKNOWN) {
                resolveContent();
                markDamaged();
            }
            LOG.debug((getSpecification().isOpen() ? "close" : "open") + " node " + getContent().getNaked());
            if (canOpen() == NodeSpecification.CAN_OPEN) {
                final View newView = replaceWithSpecification.createView(getContent(), getViewAxis());
                getParent().replaceView(getView(), newView);
            }
        } else if (y < top && x > left && click.button1()) {
            if (canOpen() == NodeSpecification.UNKNOWN) {
                resolveContent();
                markDamaged();
            }
            selectNode();
        } else {
            super.firstClick(click);
        }
    }

    @Override
    public int getBaseline() {
        return wrappedView.getBaseline() + baseline;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        // size.extendHeight(2 * VPADDING);
        size.ensureWidth(left + HPADDING + icon.getSize().getWidth() + text.getSize().getWidth() + HPADDING + right);
        return size;
    }

    @Override
    public void objectActionResult(final NakedObject result, final Location at) {
        if (getContent() instanceof OneToManyField && result instanceof NakedObject) {
            // same as InternalCollectionBorder
            final OneToManyField internalCollectionContent = (OneToManyField) getContent();
            final OneToManyAssociation field = internalCollectionContent.getOneToManyAssociation();
            final NakedObject target = ((ObjectContent) getParent().getContent()).getObject();

            final Consent about = field.isValidToAdd(target, result);
            if (about.isAllowed()) {
                field.addElement(target, result);
            }
        }
        super.objectActionResult(result, at);
    }

    private void resolveContent() {
        NakedObject parent = getParent().getContent().getNaked();
        if (!(parent instanceof NakedObject)) {
            parent = getParent().getParent().getContent().getNaked();
        }

        if (getContent() instanceof FieldContent) {
            final NakedObjectAssociation field = ((FieldContent) getContent()).getField();
            NakedObjectsContext.getPersistenceSession().resolveField(parent, field);
        } else if (getContent() instanceof CollectionContent) {
            NakedObjectsContext.getPersistenceSession().resolveImmediately(parent);
        } else if (getContent() instanceof CollectionElement) {
            NakedObjectsContext.getPersistenceSession().resolveImmediately(getContent().getNaked());
        }
    }

    @Override
    public void secondClick(final Click click) {
        final int x = click.getLocation().getX();
        final int y = click.getLocation().getY();
        if (y < top && x > left) {
            if (canOpen() == NodeSpecification.UNKNOWN) {
                resolveContent();
                markDamaged();
            }
            final Location location = getAbsoluteLocation();
            location.translate(click.getLocation());
            getWorkspace().addWindowFor(getContent().getNaked(), location);
        } else {
            super.secondClick(click);
        }
    }

    private void selectNode() {
        if (getViewAxis() instanceof SelectableViewAxis) {
            ((SelectableViewAxis) getViewAxis()).selected(getView());
        }
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/TreeNodeBorder";
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        final Bounds bounds = new Bounds(left + 1, 0, getSize().getWidth() - left - BORDER, top);
        if (bounds.contains(mouseLocation)) {
            return ViewAreaType.CONTENT;
        } else {
            return super.viewAreaType(mouseLocation);
        }
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        super.viewMenuOptions(options);
        TreeDisplayRules.menuOptions(options);

        options.add(new AbstractUserAction("Select node") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                selectNode();
            }

            @Override
            public String getDescription(final View view) {
                return "Show this node in the right-hand pane";
            }
        });

        final NakedObject object = getView().getContent().getNaked();
        final ResolveState resolveState = (object).getResolveState();
        if (object instanceof NakedObject && (resolveState.isGhost() || resolveState.isPartlyResolved())) {
            options.add(new AbstractUserAction("Load object") {
                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    resolveContent();
                }
            });
        }
    }

    private boolean withinBox(final int x, final int y) {
        return x >= BOX_X_OFFSET && x <= BOX_X_OFFSET + BOX_SIZE && y >= (top - BOX_SIZE) / 2 && y <= (top + BOX_SIZE) / 2;
    }
}
// Copyright (c) Naked Objects Group Ltd.
