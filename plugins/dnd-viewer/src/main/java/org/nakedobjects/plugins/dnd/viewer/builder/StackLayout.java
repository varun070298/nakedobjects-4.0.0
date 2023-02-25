package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


/**
 * A stack layout stacks its components vertically, one on top of the other, working down from the top. Each
 * component is given the space it requests. Components adopt the width of the widest component when that
 * component's view specification's <code>isAligned</code> method returns <code>true</code>, or the layout's
 * <code>fixedWidth</code> flag is set (via the two parameter constructor).
 * 
 */
public class StackLayout extends AbstractBuilderDecorator {
    private final boolean fixedWidth;

    public StackLayout(final ViewBuilder design) {
        super(design);
        this.fixedWidth = false;
    }

    public StackLayout(final ViewBuilder design, final boolean fixedWidth) {
        super(design);
        this.fixedWidth = fixedWidth;
    }

    @Override
    public Size getRequiredSize(final View view) {
        int height = 0;
        int width = 0;
        final View views[] = view.getSubviews();

        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size s = v.getRequiredSize(new Size(Integer.MAX_VALUE, Integer.MAX_VALUE));
            width = Math.max(width, s.getWidth());
            height += s.getHeight();
        }

        return new Size(width, height);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void layout(final View view, final Size maximumSize) {
        final int x = 0;
        int y = 0;
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
            if (fixedWidth || v.getSpecification().isAligned()) {
                s.ensureWidth(maxWidth);
            }
            v.setSize(s);
            v.setLocation(new Location(x, y));
            y += s.getHeight();
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
