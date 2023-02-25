package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractBuilderDecorator;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public class HistogramLayout extends AbstractBuilderDecorator {
    private int width = 500;
    private int barHeight = 19;
 
    public HistogramLayout(ViewBuilder design) {
        super(design);
    }
    
    public void layout(View view, Size maximumSize) {
        View[] subviews = view.getSubviews();
         int y = 0;
        for (View bar : subviews) {
            bar.setSize(new Size(width, barHeight));
            bar.setLocation(new Location(0, y));
            y += barHeight;
        }   
    }
    
    public Size getRequiredSize(View view) {
        View[] subviews = view.getSubviews();
        int graphHeight = subviews.length * barHeight;
        return new Size(width, graphHeight);
    }

}


// Copyright (c) Naked Objects Group Ltd.
