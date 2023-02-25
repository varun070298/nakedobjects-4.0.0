package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.view.form.InternalFormSpecification;


public class ExpandableViewBorder extends AbstractBorder {
    private boolean isOpen = false;

    public ExpandableViewBorder(final View view) {
        super(view);
        left = Toolkit.defaultFieldHeight();;
    }

    @Override
    public void draw(final Canvas canvas) {
        Shape pointer;
        if (isOpen) {
            pointer = new Shape(2, left / 2);
            pointer.addVertex(left - 2, left / 2);
            pointer.addVertex(left / 2, left - 2);
        } else {
            pointer = new Shape(2, 2);
            pointer.addVertex(2, left - 2);
            pointer.addVertex(left / 2, 2 + (left -2) / 2 );
        }
        canvas.drawSolidShape(pointer, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1));

        super.draw(canvas);
    }

    @Override
    public void firstClick(final Click click) {
        if (click.getLocation().getX() < left) {
            isOpen = !isOpen;
            
            View parent = wrappedView.getParent();
            
            getViewManager().removeFromNotificationList(wrappedView);
            if (isOpen) {
                wrappedView = new InternalFormSpecification().createView(getContent(), null);
            } else {
                ViewRequirement requirement = new ViewRequirement(getContent(), ViewRequirement.CLOSED);
                wrappedView = Toolkit.getViewFactory().createView(requirement );
            }
            setView(this);
            setParent(parent);
            getParent().invalidateLayout();
        } else {
            super.firstClick(click);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
