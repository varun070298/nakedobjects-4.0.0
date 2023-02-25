package org.nakedobjects.plugins.dnd.viewer.border;

import java.awt.event.KeyEvent;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.action.CloseViewOption;
import org.nakedobjects.plugins.dnd.viewer.content.RootObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dnd.viewer.view.simple.DragViewOutline;

/**
 * A border for objects providing
 * <ol>
 *  <li>Ability to drag out a new view of the object.</li>
 *  <li>State change when moving over object.
 *  <li>Feedback of the state of the view, eg drop valid, identified etc.
 *  </ol>
 */
public class ObjectBorder extends AbstractBorder {
    private static final int BORDER = 13;

    public ObjectBorder(final int size, final View wrappedView) {
        super(wrappedView);

        top = size;
        left = size;
        bottom = size;
        right = size + BORDER;
    }

    public ObjectBorder(final View wrappedView) {
        this(1, wrappedView);
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        super.debugDetails(debug);
        debug.appendln("border", top + " pixel(s)");
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (drag.getLocation().getX() > getSize().getWidth() - right) {
            if (getContent().getNaked() == null) {
                return null;
            }
            final View dragOverlay = new DragViewOutline(getView());
            return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        Color color = null;
        final ViewState state = getState();
        final boolean hasFocus = getViewManager().hasFocus(getView());
        if (state.canDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_VALID);
        } else if (state.cantDrop()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_INVALID);
        } else if (hasFocus) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
        } else if (state.isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2);
        }
        final Size s = getSize();

        if (getContent().isPersistable() && getContent().isTransient()) {
            final int x = s.getWidth() - 13;
            final int y = 0;
            final Image icon = ImageFactory.getInstance().loadIcon("transient", 8, null);
            if (icon == null) {
                canvas.drawText("*", x, y + Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent(), Toolkit.getColor(ColorsAndFonts.COLOR_BLACK), Toolkit
                        .getText(ColorsAndFonts.TEXT_NORMAL));
            } else {
                canvas.drawImage(icon, x, y, 12, 12);
            }
        }

        if (color != null) {
            if (hasFocus) {
                final int xExtent = s.getWidth() - left;
                for (int i = 0; i < left; i++) {
                    canvas.drawRectangle(i, i, xExtent - 2 * i, s.getHeight() - 2 * i, color);
                }
            } else {
                final int xExtent = s.getWidth();
                for (int i = 0; i < left; i++) {
                    canvas.drawRectangle(i, i, xExtent - 2 * i, s.getHeight() - 2 * i, color);
                }
                canvas.drawLine(xExtent - BORDER, top, xExtent - BORDER, top + s.getHeight(), color);
                canvas.drawSolidRectangle(xExtent - BORDER + 1, top, BORDER - 2, s.getHeight() - 2 * top, Toolkit
                        .getColor(ColorsAndFonts.COLOR_SECONDARY3));
            }
        }
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

    public void keyPressed(KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_SPACE && isNodeSelectable()) {
            selectNode();
        } else {
            super.keyPressed(key);
        }
    }

    private boolean isNodeSelectable() {
        return getViewAxis() instanceof SelectableViewAxis;
    }
    
    @Override
    public void firstClick(final Click click) {
        final int x = click.getLocation().getX();
        final int y = click.getLocation().getY();
        if (withinSelectorBounds(x, y) && click.button1() && isNodeSelectable()) {
            selectNode();
        } else {
            super.firstClick(click);
        }
    }

    private void selectNode() {
        ((SelectableViewAxis) getViewAxis()).selected(getView());
    }

    private boolean withinSelectorBounds(final int x, final int y) {
        return true;
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        super.viewMenuOptions(options);

        if (isNodeSelectable()) {
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
        }
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/ObjectBorder [" + getSpecification() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
