package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.ObjectView;


public class FallbackView extends ObjectView {

    public static class Specification implements ViewSpecification {
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return true;
        }

        public View createView(final Content content, final ViewAxis axis) {
            return new FallbackView(content, this, axis);
        }

        public String getName() {
            return "Fallback";
        }

        public boolean isAligned() {
            return false;
        }

        public boolean isOpen() {
            return false;
        }

        public boolean isReplaceable() {
            return false;
        }
        
        public boolean isResizeable() {
            return false;
        }

        public boolean isSubView() {
            return false;
        }
    }

    protected FallbackView(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final Size size = getSize();
        final int width = size.getWidth() - 1;
        final int height = size.getHeight() - 1;
        canvas.drawSolidRectangle(0, 0, width, height, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
        canvas.drawSolidRectangle(0, 0, 10, height, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY2));
        canvas.drawLine(10, 0, 10, height - 2, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
        canvas.drawRectangle(0, 0, width, height, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
        canvas.drawText(getContent().title(), 14, getBaseline(), Toolkit.getColor(ColorsAndFonts.COLOR_BLACK), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
    }

    @Override
    public int getBaseline() {
        return 14;
    }

    @Override
    public Size getMaximumSize() {
        return new Size(150, 20);
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return mouseLocation.getX() <= 10 ? ViewAreaType.VIEW : ViewAreaType.CONTENT;
    }
}
// Copyright (c) Naked Objects Group Ltd.
