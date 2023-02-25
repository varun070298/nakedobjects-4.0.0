package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;


public class IconBorder extends AbstractBorder {
    private final static Text TITLE_STYLE = Toolkit.getText(ColorsAndFonts.TEXT_TITLE);
    private final int baseline;
    private final int titlebarHeight;
    private final IconGraphic icon;
    private final TitleText text;

    public IconBorder(final View wrappedView) {
        super(wrappedView);

        icon = new IconGraphic(this, TITLE_STYLE);
        text = new ObjectTitleText(this, TITLE_STYLE);
        titlebarHeight =  VPADDING + icon.getSize().getHeight() + 1;

        top = titlebarHeight + VPADDING;
        left = right = HPADDING;
        bottom = VPADDING;

        baseline = VPADDING + icon.getBaseline() + 1;
    }

    @Override
    public void debugDetails(final DebugString debug) {
        super.debugDetails(debug);
        debug.appendln("titlebar", top - titlebarHeight);
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (overBorder(drag.getLocation())) {
            final View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
            return new ContentDrag(this, drag.getLocation(), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    @Override
    public void draw(final Canvas canvas) {
        int x = left - 2;

        if (Toolkit.debug) {
            canvas.drawDebugOutline(new Bounds(getSize()), baseline, Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_DRAW));
        }

        // icon & title
        icon.draw(canvas, x, baseline);
        x += icon.getSize().getWidth();
        x += View.HPADDING;
        text.draw(canvas, x, baseline);

        // components
        super.draw(canvas);
    }

    @Override
    public int getBaseline() {
        return wrappedView.getBaseline() + baseline + titlebarHeight;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        size.ensureWidth(left + icon.getSize().getWidth() + View.HPADDING + text.getSize().getWidth() + right);
        return size;
    }

    @Override
    public void firstClick(final Click click) {
        final int y = click.getLocation().getY();
        if (y < top && click.button2()) {
            final Location location = new Location(click.getLocationWithinViewer());
            getViewManager().showInOverlay(getContent(), location);            
        } else {
            super.firstClick(click);
        }
    }

    @Override
    public void secondClick(final Click click) {
        final int y = click.getLocation().getY();
        if (y < top) {
            final Location location = getAbsoluteLocation();
            location.translate(click.getLocation());
            getWorkspace().addWindowFor(getContent().getNaked(), location);
        } else {
            super.secondClick(click);
        }
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        final Bounds title = new Bounds(new Location(), icon.getSize());
        title.extendWidth(left);
        title.extendWidth(text.getSize().getWidth());
        if (title.contains(mouseLocation)) {
            return ViewAreaType.CONTENT;
        } else {
            return super.viewAreaType(mouseLocation);
        }
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/WindowBorder [" + getSpecification() + "]";
    }
}
// Copyright (c) Naked Objects Group Ltd.
