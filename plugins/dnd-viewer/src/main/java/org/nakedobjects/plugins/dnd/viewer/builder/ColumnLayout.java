package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


/**
 * A column layout places component views side by side. Each component is given the space it request. A
 * components width will be give a consistent width if that component's view specification's
 * <code>isAligned</code> method returns <code>true</code>, or the layout's <code>fixedWidth</code> flag is
 * set (via the two parameter constructor).
 */
public class ColumnLayout extends AbstractBuilderDecorator {
    private final boolean fixedWidth;

    public ColumnLayout(final ViewBuilder design) {
        super(design);
        this.fixedWidth = false;
    }

    public ColumnLayout(final ViewBuilder design, final boolean fixedWidth) {
        super(design);
        this.fixedWidth = fixedWidth;
    }

    @Override
    public Size getRequiredSize(final View view) {
        int height = 0;
        int width = 0;
        int maxWidth = 0;
        final View views[] = view.getSubviews();

        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size s = v.getRequiredSize(new Size(Integer.MAX_VALUE, Integer.MAX_VALUE));
            width += s.getWidth();
            maxWidth = Math.max(maxWidth, s.getWidth());
            height = Math.max(height, s.getHeight());
        }

        if(fixedWidth) {
            width = maxWidth / 2 * views.length;
        }
        return new Size(width, height);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void layout(final View view, final Size maximumSize) {
        int x = 0;
        final int y = 0;
        final View subviews[] = view.getSubviews();

        int maxWidth = 0;
        for (int i = 0; i < subviews.length; i++) {
            final View v = subviews[i];
            v.layout(new Size(maximumSize));
            final Size s = v.getRequiredSize(new Size(maximumSize));
            maxWidth = Math.max(maxWidth, s.getWidth());
        }

        for (int i = 0; i < subviews.length; i++) {
            final View v = subviews[i];
            final Size s = v.getRequiredSize(new Size(maximumSize));
            v.setLocation(new Location(x, y));
            if (fixedWidth || v.getSpecification().isAligned()) {
                x += maxWidth / 2;
                s.setWidth(maxWidth / 2);
            } else {
                x += s.getWidth();
            }
            v.setSize(s);
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
