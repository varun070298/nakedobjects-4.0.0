package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public class SelectedBorder extends AbstractBorder {

    public SelectedBorder(View view) {
        super(view);
    }
    
    public void firstClick(Click click) {
        ((SelectableViewAxis) getView().getViewAxis()).selected(getView());
        super.firstClick(click);
    }
    
    public void draw(Canvas canvas) {
        if (((SelectableViewAxis) getView().getViewAxis()).isSelected(getView())){
            Size size = getSize();
            canvas.drawSolidRectangle(left, right, size.getWidth() - left - right, size.getHeight() - top - bottom, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3));
        }
        super.draw(canvas);
    }

}


// Copyright (c) Naked Objects Group Ltd.
