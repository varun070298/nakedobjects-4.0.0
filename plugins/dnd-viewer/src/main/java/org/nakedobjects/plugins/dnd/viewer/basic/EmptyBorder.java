package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.AbstractBorder;


public class EmptyBorder extends AbstractBorder {

    public EmptyBorder(final int width, final View view) {
        super(view);
        left = top = right = bottom = width;
    }

    public EmptyBorder(final int topBottom, final int leftRight, final View view) {
        super(view);
        left = right = bottom = leftRight;
        top = bottom = topBottom;
    }

    public EmptyBorder(final int left, final int top, final int right, final int bottom, final View view) {
        super(view);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

}
// Copyright (c) Naked Objects Group Ltd.
