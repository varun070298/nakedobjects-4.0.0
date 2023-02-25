package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
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


/**
 * Displays an error message in place of a normal field when a problem occurs, usually due to a programming
 * error, and the normal field cannot be created. A example of this is where value field is declared in a
 * naked object, but the programmer forgot to instantiate the value object, causing null to be returned
 * instead, which is an illegal value.
 */
public class FieldErrorView extends AbstractView {

    private final String error;

    public FieldErrorView(final String errorMessage) {
        super(null, null, null);
        this.error = errorMessage;
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        final Size size = getSize();
        canvas.drawSolidRectangle(0, 0, size.getWidth() - 1, size.getHeight() - 1, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        canvas.drawRectangle(0, 0, size.getWidth() - 1, size.getHeight() - 1, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK));
        canvas.drawText(error, 14, 20, Toolkit.getColor(ColorsAndFonts.COLOR_INVALID), Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
    }

    @Override
    public int getBaseline() {
        return 20;
    }

    @Override
    public Size getMaximumSize() {
        return new Size(250, 30);
    }

    @Override
    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return mouseLocation.getX() <= 10 ? ViewAreaType.VIEW : ViewAreaType.CONTENT;
    }

    public static class Specification implements ViewSpecification {
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return true;
        }

        public View createView(final Content content, final ViewAxis axis) {
            throw new NotYetImplementedException();
        }

        public String getName() {
            return "Field Error";
        }

        public boolean isAligned() {
            return false;
        }

        public boolean isSubView() {
            return false;
        }
        
        public boolean isResizeable() {
            return false;
        }

        public boolean isReplaceable() {
            return false;
        }

        public boolean isOpen() {
            return false;
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
